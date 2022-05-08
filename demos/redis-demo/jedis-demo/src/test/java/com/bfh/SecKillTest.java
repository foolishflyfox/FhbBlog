package com.bfh;

import com.bfh.jedis.RedisConstant;
import com.bfh.jedis.SecKillDemo;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.function.BiFunction;

/**
 * @author benfeihu
 */
public class SecKillTest {

    // 同时参与秒杀的人数
    private int peopleCount = 3000;
    // 库存数量
    private String productCount = "10";


    @Test
    public void testParallelSecKillV1() throws InterruptedException {
        /**
         * 结果与分析
         * scard sk:iphone11:user 结果为 167
         * get sk:iphone11:qt 结果为 -158
         * 两者相加不为 10，因为可能存在生成两个相同 id 的用户，两者在开始秒杀前的校验是否参与过秒杀都
         * 通过了，导致库存减了2次，但是只添加了一个人
         * 超卖问题: 库存数为 -158 就是因为在线程取库存时数量大于0，但是因为并发执行，导致最终结果小于0
         * 连接超时问题
         */
        testParallelSecKill(SecKillDemo::doSecKillV1);
    }

    @Test
    public void testParallelSecKillV2() throws InterruptedException {
        testParallelSecKill(SecKillDemo::doSecKillV2);
    }

    @Test
    public void testParallelSecKillV3() throws InterruptedException {
        productCount = "500";
        testParallelSecKill(SecKillDemo::doSecKillV3);
    }

    @Test
    public void testParallelSecKillWithByLua() throws InterruptedException {
        productCount = "500";
        testParallelSecKill(SecKillDemo::doSecKillByLua);
    }


    public void testParallelSecKill(BiFunction<String, String, Boolean> killSecFunc)
            throws InterruptedException {
        final String prodid = "iphone11";
        Jedis jedis = new Jedis(RedisConstant.REDIS_HOST, RedisConstant.REDIS_PORT);
        // 清空用户表
        jedis.del(SecKillDemo.getUserKey(prodid));
        // 初始化秒杀数量
        jedis.set(SecKillDemo.getQtKey(prodid), productCount);

        Thread[] threads = new Thread[peopleCount];
        Random random = new Random();
        Runnable execSecKill = () -> {
            String userId = String.valueOf(random.nextInt(10000));
            killSecFunc.apply(userId, prodid);
        };
        for (int i = 0; i < peopleCount; ++i) threads[i] = new Thread(execSecKill);
        for (int i = 0; i < peopleCount; ++i) threads[i].start();
        for (int i = 0; i < peopleCount; ++i) threads[i].join();
    }


}
