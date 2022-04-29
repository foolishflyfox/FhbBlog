package com.bfh.dao;

import com.bfh.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author benfeihu
 * 此接口用于规范针对 customer 表的常用操作
 */
public interface CustomerDao {
    // 将 cust 对象插入到数据库中
    void insert(Connection conn, Customer cust);

    // 根据指定 id 删除一条记录
    void deleteById(Connection conn, int id);

    // 根据 id 更新数据
    void updateById(Connection conn, Customer cust);

    // 根据 id 查询数据
    Customer getCustomerById(Connection conn, int id);

    // 获取所有数据
    List<Customer> getAll(Connection conn);

    // 获取数据表中的数据的条目数
    Long getCount(Connection conn);

    Date getMaxBirth(Connection conn);
}
