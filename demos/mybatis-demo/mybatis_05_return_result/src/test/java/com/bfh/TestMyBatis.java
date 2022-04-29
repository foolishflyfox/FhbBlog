package com.bfh;

import com.bfh5.bean.MyStudent;
import com.bfh5.bean.Student;
import com.bfh5.dao.StudentDao;
import com.bfh5.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author benfeihu
 */
public class TestMyBatis {
    @Test
    public void test01() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        Student student = dao.selectStudentById(1006);
        sqlSession.close();
        System.out.println(student);
    }

    @Test
    public void test02() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        int count = dao.countAllData();
        sqlSession.close();
        System.out.println("count = " + count);
    }

    @Test
    public void test03() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Integer> r = dao.selectAllIds();
        sqlSession.close();
        System.out.println(r);
    }

    @Test
    public void test04() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        Map<String, Object> studentInfo = dao.selectStudentResultMap("张三", 25);
        System.out.println(studentInfo);
    }

    @Test
    public void test05() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Map<String, Object>> studentInfos = dao.selectStudentMultiMap("张三", 22);
        System.out.println(studentInfos);
    }

    @Test
    public void test06() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectAllStudent();
        students.forEach(System.out::println);
    }

    @Test
    public void test07() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<MyStudent> myStudents = dao.selectAllMyStudent();
        myStudents.forEach(System.out::println);
    }

    @Test
    public void test08() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<MyStudent> myStudents = dao.selectAllMyStudent2();
        myStudents.forEach(System.out::println);
    }

    @Test
    public void test09() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectLike("%张%");
        students.forEach(System.out::println);
    }

    @Test
    public void test10() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectLike2("张");
        students.forEach(System.out::println);
    }

}
