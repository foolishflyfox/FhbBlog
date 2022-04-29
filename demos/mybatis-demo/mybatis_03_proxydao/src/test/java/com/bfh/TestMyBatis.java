package com.bfh;

import com.bfh3.bean.Student;
import com.bfh3.dao.StudentDao;
import com.bfh3.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author benfeihu
 */
public class TestMyBatis {
    @Test
    public void test01() {
        // 使用 mybatis 的动态代理机制，使用 SqlSession.getMapper (dao接口)
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        System.out.println(dao);
        // 调用 dao 的方法，执行数据库的操作
        List<Student> students = dao.selectStudents();
        students.forEach(System.out::println);
    }

    @Test
    public void test02() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(1008);
        student.setName("曹操");
        student.setEmail("caocao@123.com");
        student.setAge(35);
        dao.insertStudent(student);
        sqlSession.commit();
        sqlSession.close();
    }
}
