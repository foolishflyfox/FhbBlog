package com.bfh7.dao;

import com.bfh7.bean.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author benfeihu
 */
public interface StudentDao {

    @Select("select id,name,age,email from student")
    List<Student> findAll();

    @Select("select id,name,age,email from student where id = #{id}")
    Student findById(int id);

    @Select({"<script>" +
                "select id, name, age, email from student where id in (-1" +
                "<foreach item='id' collection='ids'>" +
                    ",#{id}" +
                "</foreach>" +
            ")</script>"})
    List<Student> selectWithIds(@Param("ids") List<Integer> ids);

    @Insert("insert into student(id, name, email, age) values(#{id}, #{name}, #{email}, #{age})")
    int insertWithParams(@Param("id") int id, @Param("name") String name,
                         @Param("email") String email, @Param("age") int age);

    @Insert("insert into student(id,name,email,age) values(#{student.id}," +
            "#{student.name}, #{student.email}, #{student.age})")
    int insertWithStudent(@Param("student") Student student);

    @Update("update student set age=#{stu.age}, name=#{stu.name}, email=#{stu.email} where id=#{stu.id}")
    int updateWithStudent(@Param("stu") Student student);

    @Delete("delete from student where id = #{id}")
    int deleteWithId(@Param("id") int id);

}
