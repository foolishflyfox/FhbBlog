package com.bfh6.dao;

import com.bfh6.bean.MyStudent;
import com.bfh6.bean.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author benfeihu
 */
public interface StudentDao {
    List<Student> selectStudentIf(Student student);

    List<Student> selectStudentWhere(Student student);

    List<Student> selectStudentForeach(List<Integer> ids);

    List<Student> selectStudentForeach2(List<Student> ids);

    // 使用 PageHelper 分页数据
    List<Student> selectAll();
}
