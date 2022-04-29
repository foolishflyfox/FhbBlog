package com.bfh.mybatis.test;

import com.bfh.mybatis.bean.Student;
import com.bfh.utils.MyBatisUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author benfeihu
 */
public class MyBatisTest {
    @Test
    public void testInsert() throws IOException {
        String config = "mybatis-config.xml";

        InputStream in = Resources.getResourceAsStream(config);

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);

        SqlSession sqlSession = factory.openSession(true);
        String sqlId = "com.bfh.dao.StudentDao" + "." + "insertStudent";
        // List<Student> students = sqlSession.selectList(sqlId);
        // students.forEach(System.out::println);
        Student student = new Student();
        student.setId(1005);
        student.setName("关羽");
        student.setEmail("guanyu@abc.xyz");
        student.setAge(28);
        int insertCnt = sqlSession.insert(sqlId, student);
        System.out.println("inserCnt = " + insertCnt);
        // sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testInsert2() throws IOException {

        SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
        String sqlId = "com.bfh.dao.StudentDao" + "." + "insertStudent";
        Student student = new Student();
        student.setId(1006);
        student.setName("张飞");
        student.setEmail("zhangfei@abc.xyz");
        student.setAge(29);
        int insertCnt = sqlSession.insert(sqlId, student);
        System.out.println("inserCnt = " + insertCnt);
        // sqlSession.commit();
        sqlSession.close();
    }
}
