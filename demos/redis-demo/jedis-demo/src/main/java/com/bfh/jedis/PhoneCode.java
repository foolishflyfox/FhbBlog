package com.bfh.jedis;

import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.Random;

/**
 * @author benfeihu
 */
public class PhoneCode {
    private static final long EXPIRE_SECONDS = 120;
    private static final String REDIS_HOST = "ubuntu-01";
    private static final int REDIS_PORT = 6379;
    private static Random random = new Random();

    public static void main(String[] args) {
        String phone = "13678765432";
        // 模拟验证码发送
         verifyCode(phone);
        // 验证码确认
//        getRedisCode(phone, "436388");
    }

    // 1. 生成 6 位数字验证码
    public static String getCode() {
        return String.format("%06d", random.nextInt(1000000));
    }

    // 2. 每个手机每天只能发三次，验证码放到 redis 中，设置过期时间
    public static void verifyCode(String phone) {
        // 连接 redis
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        // 拼接 key
        // 手机发送次数 key
        String countKey = getCountKey(phone);
        // 验证码 key
        String codeKey = getCodeKey(phone);
        if (jedis.get(countKey) == null) {
            jedis.setex(countKey, 24 * 60 * 60, "0");
        }
        Long count = jedis.incr(countKey);
        if (count > 3) {
            System.out.println("今天发送次数已经测过3次");
            jedis.close();
            return;
        }
        // 验证码放入 redis
        jedis.setex(codeKey, EXPIRE_SECONDS, getCode());
        System.out.println("验证码发送成功");
        jedis.close();
    }

    // 3. 验证码校验
    public static void getRedisCode(String phone, String code) {
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        String targetCode = jedis.get(getCodeKey(phone));
        if (Objects.equals(targetCode, code)) {
            System.out.println("验证码校验成功");
        } else {
            System.out.println("验证码校验失败");
        }
        jedis.close();
    }

    private static String getCountKey(String phone) {
        return "VerifyCode:" + phone + ":count";
    }
    private static String getCodeKey(String phone) {
        return "VerifyCode:" + phone + ":code";
    }
}
