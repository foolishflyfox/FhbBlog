package com.bfh.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author benfeihu
 */
public class JedisConnectionFactory {
    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大连接，表示连接池中最多能有多少个连接
        jedisPoolConfig.setMaxTotal(50);
        // 最大空闲连接，表示处于空闲状态的连接数量的上限
        jedisPoolConfig.setMaxIdle(8);
        // 最小空闲连接，如果连接长时间处于空闲就会被释放，该值设置被释放后最少留多少空闲连接
        jedisPoolConfig.setMinIdle(0);
        // 设置最长等待时间, ms。表示连接池中没有连接可用，最长等待多久
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPool = new JedisPool(jedisPoolConfig, RedisConstant.REDIS_HOST,
                RedisConstant.REDIS_PORT);
    }

    // 获取 Jedis 对象
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
