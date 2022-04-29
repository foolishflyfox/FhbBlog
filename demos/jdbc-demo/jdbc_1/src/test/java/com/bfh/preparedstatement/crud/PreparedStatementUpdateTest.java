package com.bfh.preparedstatement.crud;

import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author benfeihu
 * 使用 PreparedStatement 替代 Statement 实现对数据表的增删改查操作
 */
public class PreparedStatementUpdateTest {
    // 向 customer 表中添加一条记录
    @Test
    public void testInsert()  {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);

            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, "哪吒");
            ps.setString(2, "nezhao@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");
            ps.setDate(3, new Date(date.getTime()));
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps!=null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1. 获取数据库的连接
            conn = JDBCUtils.getConnection();
            // 2. 预编译 sql 语句，返回 PreparedStatement 实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            // 3. 填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 18);

            // 4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 资源关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }

    @Test
    public void testDelete() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "delete from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 19);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void testDelete2() {
        update("delete from customers where id = ?", 20);
    }

    @Test
    public void testUpdate2() {
        update("update `order` set order_name = ? where order_id = ?", "DD", 2);
    }

    public void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
