package com.bfh.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author benfeihu
 */
public class SecKillDemo {


    // 获取库存数量的 key
    public static String getQtKey(String prodid) {
        return "sk:" + prodid + ":qt";
    }
    // 获取用户集合的 key
    public static String getUserKey(String prodid) {
        return "sk:" + prodid + ":user";
    }

    public static Jedis createJedis() {
        return new Jedis(RedisConstant.REDIS_HOST, RedisConstant.REDIS_PORT);
    }
    public static Jedis getJedisFromPool() {
        return JedisConnectionFactory.getJedis();
    }


    public static boolean doSecKillV1(String uid, String prodid) {
        return doSecKill(uid, prodid, SecKillDemo::createJedis);
    }

    /**
     * 解决版本1 jedis 连接超时问题
     */
    public static boolean doSecKillV2(String uid, String prodid) {
        return doSecKill(uid, prodid, SecKillDemo::getJedisFromPool);
    }

    public static boolean doSecKill(String uid, String prodid, Supplier<Jedis> jedisSupplier) {
        // 1. uid 和 prodid 非空判断
        if (uid == null || prodid == null) return false;

        // 2. 连接 redis
        Jedis jedis = jedisSupplier.get();

        // 3. 拼接 key
        // 3.1 库存 key
        String qtKey = getQtKey(prodid);
        // 3.2 秒杀成功用户 key
        String userKey = getUserKey(prodid);

        // 4. 获取库存，如果库存为 null，表示秒杀还没开始
        if (jedis.get(qtKey) == null) {
            System.out.println("秒杀失败: 秒杀没有开始");
            jedis.close();
            return false;
        }

        // 5. 开始秒杀，判断用户是否重复秒杀操作
        jedis.watch(userKey, qtKey);
        if (jedis.sismember(userKey, uid)) {
            System.out.println("秒杀失败: 重复参与过秒杀");
            jedis.close();
            return false;
        }

        // 6. 判断如果商品数量，库存量小于1，秒杀结束
        if (Integer.valueOf(jedis.get(qtKey)) < 1) {
            System.out.println("秒杀失败: 库存数量小于1");
            jedis.close();
            return false;
        }

        // 7. 秒杀过程
        // 7.1 库存 -1
        jedis.decr(qtKey);
        // 7.2 把秒杀成功用户添加到 redis
        jedis.sadd(userKey, uid);

        System.out.println("秒杀成功");
        jedis.close();
        return true;
    }

    /**
     * 使用乐观锁，存在库存滞留问题
     */
    public static boolean doSecKillV3(String uid, String prodid) {
        // 1. uid 和 prodid 非空判断
        if (uid == null || prodid == null) return false;

        // 2. 连接 redis
        Jedis jedis = getJedisFromPool();

        // 3. 拼接 key
        // 3.1 库存 key
        String qtKey = getQtKey(prodid);
        // 3.2 秒杀成功用户 key
        String userKey = getUserKey(prodid);

        // 4. 获取库存，如果库存为 null，表示秒杀还没开始
        if (jedis.get(qtKey) == null) {
            System.out.println("秒杀失败: 秒杀没有开始");
            jedis.close();
            return false;
        }

        // 5. 开始秒杀，判断用户是否重复秒杀操作
        jedis.watch(userKey, qtKey);
        if (jedis.sismember(userKey, uid)) {
            System.out.println("秒杀失败: 重复参与过秒杀");
            jedis.close();
            return false;
        }

        // 6. 判断如果商品数量，库存量小于1，秒杀结束
        if (Integer.valueOf(jedis.get(qtKey)) < 1) {
            System.out.println("秒杀失败: 库存数量小于1");
            jedis.close();
            return false;
        }


        // 7. 秒杀过程
        // 使用事务
        Transaction multi = jedis.multi();
        // 组队操作
        multi.decr(qtKey);
        multi.sadd(userKey, uid);
        List<Object> results = multi.exec();

        if (results == null || results.size() == 0) {
            System.out.println("秒杀失败");
            jedis.close();
            return false;
        }

        System.out.println("秒杀成功");
        jedis.close();
        return true;
    }

    public static boolean doSecKillByLua(String uid, String prodid) {
        String luaScript =
                "local qtKey = KEYS[1];\n" +
                "local userKey = KEYS[2];\n" +
                "local userid = KEYS[3];\n" +
                "local userExists = redis.call(\"sismember\", userKey, userid);\n" +
                "if tonumber(userExists) == 1 then\n" +
                "return 2;\n" +
                "end\n" +
                "local num = redis.call(\"get\", qtKey);\n" +
                "if tonumber(num) <= 0 then\n" +
                "return 0;\n" +
                "else\n" +
                "redis.call(\"decr\", qtKey);\n" +
                "redis.call(\"sadd\", userKey, userid);\n" +
                "end\n" +
                "return 1;";

        Jedis jedis = JedisConnectionFactory.getJedis();
        String qtKey = getQtKey(prodid);
        String userKey = getUserKey(prodid);
        String sha1 = jedis.scriptLoad(luaScript);
        Object result = jedis.evalsha(sha1, 3, qtKey, userKey, uid);

        String reString = String.valueOf(result);
        boolean secKillResult = false;
        if ("2".equals(reString)) {
            System.out.println("用户重复抢购");
        } else if ("0".equals(reString)) {
            System.out.println("商品已抢空");
        } else if ("1".equals(reString)) {
            secKillResult = true;
            System.out.println("抢购成功");
        } else {
            System.out.println("抢购异常");
        }
        jedis.close();
        return secKillResult;
    }
}
