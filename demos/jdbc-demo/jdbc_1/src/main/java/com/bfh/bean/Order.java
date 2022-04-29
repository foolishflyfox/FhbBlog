package com.bfh.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


/**
 * @author benfeihu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private String orderName;
    Date orderDate;
}
