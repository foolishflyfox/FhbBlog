package com.bfh.exercise;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * @author benfeihu
 */
public class ExerTest {
    @Test
    public void test01() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        Class.forName(properties.getProperty("driverClass"));
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        Connection conn = DriverManager.getConnection(url, user, password);
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "古天乐");
        ps.setObject(2, "gutianle@qq.com");
        ps.setObject(3, "1979-9-1");
        ps.execute();
        ps.close();
        conn.close();
    }
}
