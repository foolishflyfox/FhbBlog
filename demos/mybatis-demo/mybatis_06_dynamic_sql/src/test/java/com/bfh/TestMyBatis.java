package com.bfh;

import com.bfh6.bean.MyStudent;
import com.bfh6.bean.Student;
import com.bfh6.dao.StudentDao;
import com.bfh6.utils.MyBatisUtils;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
        Student student = new Student();
//        student.setName("张三");
        student.setAge(28);
        List<Student> students = dao.selectStudentIf(student);
        students.forEach(System.out::println);
    }

    @Test
    public void test02() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
//        student.setName("张三");
//        student.setAge(28);
        List<Student> students = dao.selectStudentWhere(student);
        students.forEach(System.out::println);
    }

    @Test
    public void test03() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Integer> list = new ArrayList<>(Arrays.asList(1001, 1002, 1003));
        List<Student> students = dao.selectStudentForeach(list);
        students.forEach(System.out::println);
    }
    @Test
    public void test04() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        List<Student> list = new ArrayList<>();
        Student s1 = new Student(); s1.setId(1001);
        Student s2 = new Student(); s2.setId(1002);
        Student s3 = new Student(); s3.setId(1003);
//        list.add(s1);
//        list.add(s2);
//        list.add(s3);
        List<Student> students = dao.selectStudentForeach2(list);
        students.forEach(System.out::println);
    }

    @Test
    public void test05() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentDao dao = sqlSession.getMapper(StudentDao.class);
        // 加入 PageHelper 的方法，分页
        // pageNum 第几页，从1开始
        // pageSize 一共多少行数据
        PageHelper.startPage(3, 3);
        List<Student> students = dao.selectAll();
        students.forEach(System.out::println);
    }

    @Test
    public void testFor() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1001, 1002, 1003));
        // String sql  = "select * from student where id in (1001, 1002, 1003)";
        String sql = "select * from student where id in ";
        StringBuilder sb = new StringBuilder(sql);


        sb.append('(');
        for (Integer id: list) {
            sb.append(id).append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(')');
        System.out.println(sb.toString());
    }

}
