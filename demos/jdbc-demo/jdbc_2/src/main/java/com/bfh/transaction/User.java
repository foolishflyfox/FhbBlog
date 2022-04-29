package com.bfh.transaction;

import lombok.Data;
import lombok.Getter;

/**
 * @author benfeihu
 */
@Data
public class User {
    private String user;
    private String password;
    private int balance;
}
