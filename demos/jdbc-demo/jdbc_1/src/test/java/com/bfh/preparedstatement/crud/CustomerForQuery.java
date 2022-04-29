package com.bfh.preparedstatement.crud;

import com.bfh.bean.Customer;
import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author benfeihu
 */
public class CustomerForQuery {

    public List<Customer> queryForCustomers(String sql, Object... args)  {
        List<Customer> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i + 1, args[i]);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Customer customer = new Customer();
                for (int i = 1; i <= columnCount; ++i) {
                    Object columnValue = rs.getObject(i);
                    // 获取每个列的列名
                    String columnName = rsmd.getColumnName(i);
                    // 给 customer 对象指定的 columnName 属性赋值为 columnValue，通过反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                result.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return result;
    }

    @Test
    public void testQueryForCustomers() {
        String sql = "select id,name,birth,email from customers where id=?";
        List<Customer> customers = queryForCustomers(sql, 13);
        System.out.println(customers);

        System.out.println(queryForCustomers("select name, email from customers where name = ?", "周杰伦"));
    }

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 5);
            // 获取结果集
            resultSet = ps.executeQuery();
            // 处理结果集
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                // Object[] data = new Object[] {id, name, email, birth};
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, resultSet);
        }
    }
}
