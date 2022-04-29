package com.bfh.statement;

import com.bfh.User;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author benfeihu
 */
public class StatementDemo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("username: ");
        String userName = scan.nextLine();
        System.out.print("password: ");
        String password = scan.nextLine();

        String sql = "Select user, password from user_table where user='" + userName +
                "' and password='" + password + "'";
        User user = get(sql, User.class);
        if (user == null) {
            System.out.println("登陆失败");
        } else {
            System.out.println("登陆成功");
        }

    }
    private static <T> T get(String sql, Class<T> clazz) {
        T t = null;
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InputStream is = StatementDemo.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            // 3. 加载驱动
            Class.forName(driverClass);
            // 4. 获取连接
            connection = DriverManager.getConnection(url, user, password);
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                t = clazz.newInstance();
                for (int i = 0; i < columnCount; ++i) {
                    // 1. 获取列的名称
                    String columnName = rsmd.getColumnLabel(i+1);
                    Object columnVal = rs.getObject(columnName);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
                return t;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
