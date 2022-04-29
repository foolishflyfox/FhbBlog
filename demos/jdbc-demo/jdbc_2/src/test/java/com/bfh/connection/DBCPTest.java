package com.bfh.connection;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author benfeihu
 */
public class DBCPTest {
    @Test
    public void testGetConnection() throws SQLException {
        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("12345678");

        // 还可以设置其他涉及数据库连接池管理的属性
        source.setInitialSize(10);
        source.setMaxActive(50);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    @Test
    public void testGetConnection2()  throws Exception {
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties"));
//        properties.load(new FileInputStream(new File("src/test/resources/dbcp.properties")));
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
