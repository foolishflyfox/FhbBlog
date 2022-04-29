package com.bfh.exercise;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author benfeihu
 */
public class Exer1 {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("name: ");
        String name = scanner.next();
        System.out.print("email: ");
        String email = scanner.next();
        System.out.print("birthday: ");
        String birthday = scanner.next();
        int insertCnt = insert(name, email, birthday);
        if (insertCnt > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    public static int insert(String name, String email, String birth) throws Exception{
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
        ps.setObject(1, name);
        ps.setObject(2, email);
        ps.setObject(3, birth);
        // ps.execute();
        int effectRowCnt = ps.executeUpdate();
        ps.close();
        conn.close();
        return effectRowCnt;
    }
}
