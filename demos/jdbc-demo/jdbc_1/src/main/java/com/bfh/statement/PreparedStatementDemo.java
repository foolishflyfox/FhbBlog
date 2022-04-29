package com.bfh.statement;

import com.bfh.User;
import com.bfh.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author benfeihu
 * 除了解决 Statement 的拼串，sql 注入外，还有如下好处：
 * 1. PreparedStatement 操作 Blob 的数据，而 Statement 做不到
 * 2. PreparedStatement 批量操作方面效率高
 */
public class PreparedStatementDemo {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("username: ");
        String userName = scan.nextLine();
        System.out.print("password: ");
        String password = scan.nextLine();

//        String sql = "Select user, password from user_table where user='" + userName +
//                "' and password='" + password + "'";
//        User user = get(sql, User.class);
        String sql = "Select user, password from `user_table` where user=? and password=?";
        List<User> users = getInstance(sql, User.class, userName, password);
        if (users.isEmpty()) {
            System.out.println("登陆失败");
        } else {
            System.out.println("登陆成功");
        }

    }

    public static <T> List<T> getInstance(String sql, Class<T> clazz, Object... args) {
        List<T> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i + 1, args[i]);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCnt = rsmd.getColumnCount();
            while (rs.next()) {
                T tmp = clazz.newInstance();
                for (int i = 1; i <= columnCnt; ++i) {
                    String columnName = rsmd.getColumnLabel(i);
                    Object columnValue = rs.getObject(i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(tmp, columnValue);
                }
                result.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return result;
    }
}
