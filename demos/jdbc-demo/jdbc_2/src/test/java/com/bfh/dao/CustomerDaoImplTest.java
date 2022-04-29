package com.bfh.dao;

import com.bfh.bean.Customer;
import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author benfeihu
 */
public class CustomerDaoImplTest {

    CustomerDao dao = new CustomerDaoImpl();

    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "benfeihu", "bfh@abc.xyz",
                    new Date(new java.util.Date().getTime()));
            dao.insert(conn, customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            System.out.println("添加失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn, 27);
            System.out.println("删除成功");
        } catch (Exception e) {
            System.out.println("删除失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void updateById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = new Customer(26, "apple", "apple@abc.xyz",
                    new Date(new java.util.Date().getTime()));
            dao.updateById(conn, customer);
            System.out.println("修改成功");
        } catch (Exception e) {
            System.out.println("修改失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 16);
            System.out.println("查询结果: " + customer);
        } catch (Exception e) {
            System.out.println("查询失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Customer> customers = dao.getAll(conn);
            System.out.println("查询结果:");
            customers.forEach(customer -> System.out.println("\t" + customer));
        } catch (Exception e) {
            System.out.println("查询失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            System.out.println("记录数量: " + dao.getCount(conn));
        } catch (Exception e) {
            System.out.println("查询失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println("最大生日为: " + maxBirth);
        } catch (Exception e) {
            System.out.println("查询最大生日失败");
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}