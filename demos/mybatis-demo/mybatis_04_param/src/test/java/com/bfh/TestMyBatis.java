package com.bfh;

import com.bfh4.bean.Student;
import com.bfh4.dao.StudentDao;
import com.bfh4.utils.MyBatisUtils;
import com.bfh4.vo.QueryParam;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
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
    public void test03() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectMultiObject(new QueryParam("张飞", 22));
        students.forEach(System.out::println);
    }

    @Test
    public void test04() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectMultiPosition("李四", 22);
        students.forEach(System.out::println);
    }

    @Test
    public void test05() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);

        Map<String,Object> data = new HashMap<>();
        data.put("myname", "张三");
        data.put("age", 22);

        List<Student> students = dao.selectMultiByMap(data);
        students.forEach(System.out::println);
    }

    @Test
    public void test02() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectUse$("1' or '1'='1");
        students.forEach(System.out::println);
    }

    @Test
    public void test06() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.selectUse$Order("email");
        students.forEach(System.out::println);
    }
}
