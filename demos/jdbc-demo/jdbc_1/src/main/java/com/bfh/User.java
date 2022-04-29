package com.bfh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author benfeihu
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String user;
    private String password;
}
