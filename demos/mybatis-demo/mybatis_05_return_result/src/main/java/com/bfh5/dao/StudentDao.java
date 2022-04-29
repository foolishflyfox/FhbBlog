package com.bfh5.dao;

import com.bfh5.bean.MyStudent;
import com.bfh5.bean.Student;
import com.bfh5.vo.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author benfeihu
 */
public interface StudentDao {
//    List<Student> selectStudents();
//    int insertStudent(Student student);
    Student selectStudentById(@Param("id") int id);

    // 多个参数：命名参数，在形参定义的前面加上 @Param("xxx")
    List<Student> selectMultiParam(@Param("myname") String name, @Param("myage") int age);

    int countAllData();

    List<Integer> selectAllIds();

    Map<String, Object> selectStudentResultMap(@Param("name") String name, @Param("age") int age);

    List<Map<String, Object>> selectStudentMultiMap(@Param("name") String name, @Param("age") int age);

    // 使用 resultMap 定义映射关系
    List<Student> selectAllStudent();

    List<MyStudent> selectAllMyStudent();

    List<MyStudent> selectAllMyStudent2();

    // 第一种模糊查询，在 java 代码中指定 like 的内容
    List<Student> selectLike(String name);

    List<Student> selectLike2(String name);
}
