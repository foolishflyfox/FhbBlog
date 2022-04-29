package com.bfh4.dao;

import com.bfh4.bean.Student;
import com.bfh4.vo.QueryParam;
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

    List<Student> selectMultiObject(QueryParam param);

    List<Student> selectMultiPosition(String name, Integer age);

    List<Student> selectMultiByMap(Map<String, Object> map);

    List<Student> selectUse$(@Param("name") String name);

    List<Student> selectUse$Order(@Param("colName") String colName);
}
