package com.bfh;

import com.bfh.transaction.User;
import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author benfeihu
 * 1. 什么叫数据库事务
 * 事务：一组逻辑操作单元，使数据从一种状态变换到另一种状态。
 * 事务处理: 保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操作时，
 *     要么所有的事务都被提交(commit)，那么这些修改就被永久保存下来；要么DBMS将放弃所做的所有修改，整个事务回滚(rollback)
 *     到最初状态
 * JDBC 事务处理：
 *     数据一旦提交，就不可回滚
 * 哪些操作会导致数据的自动提交
 *     > DDL 操作一旦执行，都会自动提交。set autocommit = false 对 DDL 操作失效
 *     > DML 默认情况下，一旦执行就会自动提交
 *         > 我们可以通过 set autocommit = false 的方式取消 DML 操作的自动提交
 *     > 关闭连接的时候
 *
 */
public class TransactionTest {

    /**
     * update user_table set balance = balance - 100 where user='AA'
     * update user_table set balance = balance + 100 where user='BB'
     */
    @Test
    public void testUpdate() {
        try {
            String sql1 = "update user_table set balance = balance - 100 where user=?";
            update(sql1, "AA");
            /// System.out.println(10 / 0);
            String sql2 = "update user_table set balance = balance + 100 where user=?";
            update(sql2, "BB");

            System.out.println("转账成功");
        } catch (Exception e) {
            System.out.println("转账失败");
        }
    }

    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        int r = 0;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
            r = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return r;
    }

    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        int r = 0;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
            r = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
        return r;
    }

    @Test
    public void testUpdateWithTx() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user=?";
            update(conn, sql1, "AA");
//            System.out.println(10 / 0);
            String sql2 = "update user_table set balance = balance + 100 where user=?";
            update(conn, sql2, "BB");
            conn.commit();
            System.out.println("转账成功");
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("转账失败");
        } finally {
            try {
                // 针对数据库连接池
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();

         conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        System.out.println("tx level = " + conn.getTransactionIsolation());
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user=?";
        List<User> users = getInstance(conn, sql, User.class, "CC");
        System.out.println(users);
    }
    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(conn, sql, 5000, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束");
    }
    // 通用的查询操作，用于返回一条数据表中的一条记录(version2.0 考虑上事务)
    public <T> List<T> getInstance(Connection conn, String sql, Class<T> clazz, Object... args) {
        List<T> result = new ArrayList<>();
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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return result;
    }

}
