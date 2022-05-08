package com.bfh;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author benfeihu
 */
public class BaseJedisTest {

    private Jedis jedis = null;

    @Before
    public void setUp() {
        // 建立连接
        jedis = new Jedis("ubuntu-01", 6379);
        // 设置密码
        jedis.auth("12345678");
        // 选择库
        jedis.select(0);
    }

    @Test
    public void testString() {
        // 插入数据，方法名称就是 redis 命令名称
        String result = jedis.set("name", "张三");
        System.out.println("result = " + result);
        // 获取数据
        String name = jedis.get("name");
        System.out.println(name);
//        Assert.assertEquals("zhangsan", name);
    }

    @Test
    public void testHashSet() {
        HashMap<String, String> user = new HashMap<>();
        user.put("id", "1");
        user.put("name", "zhangsan");
        user.put("age", "21");
        Long result = jedis.hset("user:1", user);
        System.out.println("result = " + result);
    }

    @Test
    public void testHashGet() {
        Map<String, String> user = jedis.hgetAll("user:1");
        System.out.println(user);
    }

    @Test
    public void testListAdd() {
        Long result = jedis.lpush("list1", new String[]{"a", "b", "c"});
        System.out.println("result = " + result);
    }

    @Test
    public void testListGet() {
        List<String> results = jedis.lrange("list1", 0, -1);
        System.out.println(results);
    }

    @Test
    public void testSet() {
        Long result = jedis.sadd("set1", new String[]{"a", "b", "a", "c"});
        System.out.println("result = " + result);
    }

    @Test
    public void testSetGet() {
        Set<String> result = jedis.smembers("set1");
        System.out.println(result);
    }

    @Test
    public void testSortedSet() {
        Map<String, Double> topn = new HashMap<>();
        topn.put("java", 98.0);
        topn.put("c++", 84.2);
        topn.put("python", 92.4);
        jedis.zadd("topn", topn);
    }

    @Test
    public void testSortedSetGet() {
        Set<Tuple> topn = jedis.zrevrangeWithScores("topn", 0, -1);
        System.out.println(topn);
    }

    @After
    public void tearDown() {
        if (null != jedis) {
            jedis.close();
            jedis = null;
        }
    }
}
