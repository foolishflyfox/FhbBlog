package com.bfh.dbutils;

import com.bfh.bean.Customer;
import com.bfh.util.JDBCPoolUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author benfeihu
 */
public class QueryRunnerTest {
    @Test
    public void testInsert() throws Exception{
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "insert into customers(name, email, birth) values(?,?,?)";
        int insertCount = runner.update(conn, sql, "蔡徐坤", "caixunkun@abc.com", "1992-1-1");
        System.out.println("添加了 " + insertCount + "条记录");
    }

    /**
     * BeanHandler: 是 ResultSetHandler 接口的实现类，用于封装表中的一条记录
     * @throws SQLException
     */
    @Test
    public void testQuery() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select id,name,email,birth from customers where id=?";
        BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql, handler, 24);
        System.out.println(customer);
    }

    @Test
    public void testQuery2() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select id,name,email,birth from customers where id > ? and id < ?";
        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
        List<Customer> customers = runner.query(conn, sql, handler, 5, 24);
        customers.forEach(System.out::println);
    }

    @Test
    public void testQuery3() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select id,name,email,birth from customers where id=?";
        MapHandler mapHandler = new MapHandler();
        Map<String, Object> result = runner.query(conn, sql, mapHandler, 24);
        System.out.println(result);
    }

    @Test
    public void testQuery4() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select id,name,email,birth from customers where id<?";
        MapListHandler mapListHandler = new MapListHandler();
        List<Map<String, Object>> result = runner.query(conn, sql, mapListHandler, 24);
        result.forEach(System.out::println);
    }

    @Test
    public void testQuery5() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select count(*) from customers";
        ScalarHandler<Long> longScalarHandler = new ScalarHandler<>();
        Long r = runner.query(conn, sql, longScalarHandler);
        System.out.println("data count = " + r);
    }

    @Test
    public void testQuery6() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select max(birth) from customers";
        ScalarHandler<Date> longScalarHandler = new ScalarHandler<>();
        Date r = runner.query(conn, sql, longScalarHandler);
        System.out.println("max birth = " + r);
    }

    @Test
    public void testQuery7() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCPoolUtils.getConnectionWithDruid();
        String sql = "select id,name,email,birth from customers where id <= ?";
        ResultSetHandler<List<Customer>> handler = new ResultSetHandler<List<Customer>>() {
            @Override
            public List<Customer> handle(ResultSet rs) throws SQLException {
                List<Customer> result = new ArrayList<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    Customer customer = new Customer();
                    for (int i = 1; i <= columnCount; ++i) {
                        try {
                            Field field = Customer.class.getDeclaredField(rsmd.getColumnLabel(i));
                            field.setAccessible(true);
                            field.set(customer, rs.getObject(i));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    result.add(customer);
                }
                return result;
            }
        };
        List<Customer> customers = runner.query(conn, sql, handler, 16);
        customers.forEach(System.out::println);
    }

}
