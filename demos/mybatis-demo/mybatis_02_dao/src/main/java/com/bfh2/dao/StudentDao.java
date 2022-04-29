package com.bfh2.dao;

import com.bfh2.bean.Student;

import java.util.List;

/**
 * @author benfeihu
 */
public interface StudentDao {
    List<Student> selectStudents();
    int insertStudent(Student student);
}
