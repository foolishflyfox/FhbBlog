package com.bfh3.dao;

import com.bfh3.bean.Student;

import java.util.List;

/**
 * @author benfeihu
 */
public interface StudentDao {
    List<Student> selectStudents();
    int insertStudent(Student student);
}
