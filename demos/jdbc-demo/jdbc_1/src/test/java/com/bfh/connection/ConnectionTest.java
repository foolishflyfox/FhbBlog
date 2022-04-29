package com.bfh.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author benfeihu
 */
public class ConnectionTest {

    @Test
    public void testConnection1() throws SQLException {
        // 获取 Driver 实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        // jdbc 主协议，mysql 子协议
        String url = "jdbc:mysql://localhost:3306/test";
        // 将用户名与密码封装在 properties
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "12345678");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    @Test
    public void testConnection2() throws Exception {
        // 获取 Driver 实现类对象，使用反射
        Class<?> clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "12345678");
        driver.connect("jdbc:mysql://localhost:3306/test", info);
        System.out.println(driver);
    }

    @Test
    public void testConnection3() throws Exception {
        Class<?> clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        DriverManager.registerDriver(driver);
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "12345678";
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    @Test
    public void testConnection4() throws Exception {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "12345678";
        Class<?> clazz = Class.forName("com.mysql.cj.jdbc.Driver");
//        Driver driver = (Driver) clazz.newInstance();
//        DriverManager.registerDriver(driver);

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    // 将配置信息存在配置文件中
    // 好处：与具体的数据库解耦，实现数据与代码的分离；修改配置信息不需要重新程序打包；
    @Test
    public void testConnection5() throws IOException, ClassNotFoundException, SQLException {
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);

        Class.forName(pros.getProperty("driverClass"));
        Connection connection = DriverManager.getConnection(pros.getProperty("url"), pros.getProperty("user"), pros.getProperty("password"));
        System.out.println(connection);
    }
}
