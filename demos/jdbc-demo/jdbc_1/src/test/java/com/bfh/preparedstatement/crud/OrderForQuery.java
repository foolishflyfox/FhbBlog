package com.bfh.preparedstatement.crud;

import com.bfh.bean.Order;
import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author benfeihu
 */
public class OrderForQuery {
    @Test
    public void testQuery1() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select order_id , order_name , order_date  from `order` where order_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, 4);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCnt = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnCnt; ++i) {
                System.out.printf(" %s=%s ", rsmd.getColumnLabel(i), rs.getObject(i));
            }
        }
        JDBCUtils.closeResource(conn, ps, rs);
    }

    /**
     * 针对表的字段名与类的属性名不相同的时候
     * 1. 必须声明 sql 时，使用类的属性名来命名字段的别名
     * 2. 使用 ResultSetMetaData 时，使用 getColumnLabel 来替代 getColumnName，获取列的别名
     *      说明：如果 sql 中没有给字段起别名，getColumnLabel() 获取的就是列名
     */
    @Test
    public void testQuery2() {
        String sql = "select order_id orderId, order_name orderName,order_date orderDate from `order` where order_id=?";
        List<Order> orders = orderForQuery(sql, 4);
        System.out.println(orders);
    }

    public List<Order> orderForQuery(String sql, Object... args) {
        List<Order> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Order order = new Order();
                for (int i = 1; i <= columnCount; ++i) {
                    // 获取每个列的列值
                    Object columnValue = rs.getObject(i);
                    String columnLabel = rsmd.getColumnLabel(i);

                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                result.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return result;
    }
}
