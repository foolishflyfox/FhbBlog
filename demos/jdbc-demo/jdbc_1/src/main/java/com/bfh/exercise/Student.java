package com.bfh.exercise;

import lombok.*;

/**
 * @author benfeihu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    // Type | IDCard             | ExamCard        | StudentName | Location    | Grade
    private int flowId;
    private int type;
    private String IDCard;
    private String examCard;
    private String name;
    private String location;
    private int grade;

}
