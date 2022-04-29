package com.bfh.preparedstatement.crud;

import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author benfeihu
 * 使用 PreparedStream 批量实现批量数据的操作
 * update \ delete 本身就加油批量操作的效果
 * 此时的批量操作，只要指的是批量插入
 *
 * 题目：向 goods 表中插入 20000 条数据
 * create table goods(
 *   id int primary key auto_increment,
 *   name varchar(25)
 * );
 *
 * 方式一：Statement
 *   Connction conn = JDBCUtil.getConnection();
 *   Statement st = conn.createStatement();
 *   for (int i = 1; i < 20000; i++) {
 *       String sql = "insert into goods(name) values('name_" + i + "')";
 *       st.execute(sql);
 *   }
 */
public class InsertTest {

    static final int INSERT_COUNT = 1000000;

    // 方式二：批量插入的方式而：使用 PreparedStatement
    // 10361: 10s 左右 (20000条数据)
    @Test
    public void testInsert1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < INSERT_COUNT; ++i) {
                ps.setString(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("use time is " + (end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    // 方式三：
    // 1. addBatch()  /  insertBatch()  / clearBatch()
    // 878ms (20000条数据)
    // 8591ms (1000000条数据)
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < INSERT_COUNT; ++i) {
                ps.setString(1, "name_" + i);

                // 1. 攒 sql
                ps.addBatch();
                if (i % 500 == 0 || i == INSERT_COUNT-1) {
                    // 2. 执行 batch
                    ps.executeBatch();
                    // 3. 清空 batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("use time is " + (end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 方式3：不允许自动提交
     * 6618 (1000000条数据)
     */
    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            // 设置不允许自动提交数据
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < INSERT_COUNT; ++i) {
                ps.setString(1, "name_" + i);

                // 1. 攒 sql
                ps.addBatch();
                if (i % 500 == 0 || i == INSERT_COUNT-1) {
                    // 2. 执行 batch
                    ps.executeBatch();
                    // 3. 清空 batch
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("use time is " + (end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
