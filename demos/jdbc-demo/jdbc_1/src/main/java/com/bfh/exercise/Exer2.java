package com.bfh.exercise;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author benfeihu
 */
public class Exer2 {

    public static void main(String[] args) throws Exception {
        // insert();
        // queryWithCardIdOrExamCard();
        deleteWithExamCard();
    }



    public static void insert() throws Exception {
        // Type | IDCard             | ExamCard        | StudentName | Location    | Grade
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type: ");
        int type = scanner.nextInt();
        System.out.print("IDCard: ");
        String idCard = scanner.next();
        System.out.print("ExamCard: ");
        String examCard = scanner.next();
        System.out.print("StudentName: ");
        String studentName = scanner.next();
        System.out.print("Location: ");
        String location = scanner.next();
        System.out.print("Grade: ");
        int grade = scanner.nextInt();
        String sql = "insert into examstudent(Type, IDCard, ExamCard, StudentName, Location, Grade) values (?,?,?,?,?,?)";
        int insertCount = insertHelper(sql, type, idCard, examCard, studentName, location, grade);
        if (insertCount > 0) System.out.println("添加成功");
        else System.out.println("添加失败");
    }

    // 问题1. 向 examstudent 表中添加一条记录
    public static int insertHelper(String sql, Object... args) throws Exception{
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        Class.forName(properties.getProperty("driverClass"));
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
        // ps.execute();
        int effectRowCnt = ps.executeUpdate();
        ps.close();
        conn.close();
        return effectRowCnt;
    }

    public static void queryWithCardIdOrExamCard() {
        // FlowID, Type | IDCard             | ExamCard        | StudentName | Location    | Grade
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入查询类型（a:准考证号, b:身份证号）: ");
        String queryType = scanner.next();
        String prefix = "select FlowId flowId, Type type, IDCard, ExamCard examCard," +
                " StudentName name, Location location, Grade grade from examstudent ";
        List<Student> students = null;
        if ("a".equalsIgnoreCase(queryType)) {
            System.out.print("请输入准考证号: ");
            String examCard = scanner.next();
            String sql = prefix + "where ExamCard = ?";
            students = getInstance(sql, Student.class, examCard);
        } else if ("b".equalsIgnoreCase(queryType)){
            System.out.print("请输入身份证号: ");
            String idCard = scanner.next();
            String sql = prefix + "where IDCard = ?";
            students = getInstance(sql, Student.class, idCard);
        } else {
            System.out.println("wrong chose!!!");
            return;
        }
        System.out.println("查询结果：" + (students.isEmpty() ? "查无此人":""));
        for (Student student: students) {
            System.out.println(student);
        }
    }

    public static <T> List<T> getInstance(String sql, Class<T> clazz, Object... args)  {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        List<T> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            properties.load(inputStream);
            Class.forName(properties.getProperty("driverClass"));
            conn = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCnt = rsmd.getColumnCount();
            while (rs.next()) {
                T tmp = clazz.newInstance();
                for (int i = 1; i <= columnCnt; ++i) {
                    String columnName = rsmd.getColumnLabel(i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(tmp, rs.getObject(i));
                }
                result.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void deleteWithExamCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入学生的考号: ");
        String examCard = scanner.next();
        String sql = "delete from examstudent where ExamCard = ?";
        int deletedCount = deleteInstance(sql, examCard);
        if (deletedCount > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("查无此人");
        }
    }

    public static int deleteInstance(String sql, Object... args) {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            properties.load(is);
            Class.forName(properties.getProperty("driverClass"));
            conn = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
