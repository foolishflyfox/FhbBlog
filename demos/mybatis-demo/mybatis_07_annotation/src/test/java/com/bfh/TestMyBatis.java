package com.bfh;

import com.bfh7.bean.Student;
import com.bfh7.dao.StudentDao;
import com.bfh7.utils.MyBatisUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author benfeihu
 */
public class TestMyBatis {

    @Test
    public void test01() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> students = dao.findAll();
        students.forEach(System.out::println);
    }

    @Test
    public void test02() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        Student student = dao.findById(1110);
        System.out.println(student == null);
        System.out.println(student);
        // System.out.println(student.getName() == null);
    }

    @Test
    public void test03() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        // List<Student> students = dao.selectWithIds(Arrays.asList(1002, 1004, 1006));
        List<Student> students = dao.selectWithIds(Collections.emptyList());
        students.forEach(System.out::println);
    }

    @Test
    public void testInsert() throws Exception {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(is);
        SqlSession sqlSession = factory.openSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        int insertCount = dao.insertWithParams(1010, null, "sunquan@xxx.com", 30);
        sqlSession.commit();
        System.out.println("插入数据: " + insertCount + " 条");
        sqlSession.close();
    }

    @Test
    public void testInsert2() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(1012);
        student.setName("周瑜");
        student.setEmail(null);
        student.setAge(23);
        int insertCount = dao.insertWithStudent(student);
        System.out.printf("插入数据: %d 条", insertCount);
        sqlSession.close();
    }

    @Test
    public void testUpdate() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        int id = 1110;
        Student student = dao.findById(id);
        if (student == null) student = new Student();
        student.setId(id);
        student.setName("黄盖");
        int updateCount = dao.updateWithStudent(student);
        sqlSession.commit();
        System.out.printf("更新 %d 条数据\n", updateCount);
        sqlSession.close();
    }

    @Test
    public void testDelete() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        int deleteCount = dao.deleteWithId(1012);
        System.out.printf("删除 %d 条数据\n", deleteCount);
        sqlSession.close();
    }

}
