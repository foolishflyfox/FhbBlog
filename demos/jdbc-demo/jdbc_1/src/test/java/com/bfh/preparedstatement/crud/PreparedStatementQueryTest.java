package com.bfh.preparedstatement.crud;

import com.bfh.bean.Customer;
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
import java.util.stream.Collectors;

/**
 * @author benfeihu
 */
public class PreparedStatementQueryTest {
    public <T> List<T> getInstance(String sql, Class<T> clazz, Object... args) {
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

    @Test
    public void testGetInstance() {
        List<Order> orders = getInstance(
                "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id>?",
                Order.class, 1);
        System.out.println(orders);

        List<Customer> customers = getInstance(
                "select id,name,email,birth from `customers` where id>?", Customer.class, 2);
        customers.forEach(System.out::println);


    }
}
