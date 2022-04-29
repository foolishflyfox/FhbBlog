package com.bfh.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author benfeihu
 */
public class JDBCPoolUtils {
    private static DataSource cpds = new ComboPooledDataSource("hellc3p0");
    private static DataSource dbcp = null;
    private static DataSource druid = null;
    static {
        try {
            Properties properties = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(is);
            dbcp = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Properties props = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            props.load(is);
            druid = DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 使用 c3p0 数据库连接池技术
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }

    public static Connection getConnectionWithDBCP() throws SQLException {
        Connection conn = dbcp.getConnection();
        return conn;
    }

    public static Connection getConnectionWithDruid() throws SQLException {
        Connection conn = druid.getConnection();
        return conn;
    }
}
