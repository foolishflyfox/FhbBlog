package com.bfh.mybatis.bean;

import lombok.Data;

/**
 * @author benfeihu
 */
@Data
public class Student {
    // 定义属性，目前要求是属性名和列名一致
    private int id;
    private String name;
    private String email;
    private int age;
}
