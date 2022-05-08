package com.bfh;

import com.bfh.jedis.JedisConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author benfeihu
 */
public class JedisPoolTest {
    private Jedis jedis;

    @Before
    public void before() {
        jedis = JedisConnectionFactory.getJedis();
    }

    @Test
    public void test01() {
        jedis.set("hello", "world");
        System.out.println(jedis.get("hello"));
    }

    @After
    public void after() {
        // 如果是在连接池中，并不会真正关闭
        jedis.close();
    }
}
