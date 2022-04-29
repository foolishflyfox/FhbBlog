package com.bfh.mybatis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author benfeihu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private int id;
    private String lastName;
    private String email;
    private String gender;
}
