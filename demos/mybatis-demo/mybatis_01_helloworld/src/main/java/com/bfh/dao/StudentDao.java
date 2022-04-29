package com.bfh.dao;

import com.bfh.mybatis.bean.Student;

import java.util.List;

/**
 * @author benfeihu
 */
public interface StudentDao {
    // 查询 student 表的所有数据
    List<Student> selectStudents();

    int insertStudent(Student student);
}
