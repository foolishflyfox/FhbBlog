package com.bfh.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author benfeihu
 * ORM 编程思想(Object Relational Mapping)
 * 一个数据表对应一个 java 类
 * 表中一条记录对应 java 类的一个对象
 * 表中一个字段对应 java 类的一个属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date birth;
}
