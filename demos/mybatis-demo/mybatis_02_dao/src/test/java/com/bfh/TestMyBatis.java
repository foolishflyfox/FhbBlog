package com.bfh;

import com.bfh2.bean.Student;
import com.bfh2.dao.StudentDao;
import com.bfh2.dao.impl.StudentDaoImpl;
import org.junit.Test;

import java.util.List;

/**
 * @author benfeihu
 */
public class TestMyBatis {
    @Test
    public void test01() {
        StudentDao dao = new StudentDaoImpl();
        List<Student> students = dao.selectStudents();
        students.forEach(System.out::println);
    }

    @Test
    public void test02() {
        StudentDao dao = new StudentDaoImpl();
        Student student = new Student();
        student.setId(1007);
        student.setName("诸葛亮");
        student.setEmail("zhugeliang@xxx.com");
        student.setAge(31);
        dao.insertStudent(student);
    }
}
