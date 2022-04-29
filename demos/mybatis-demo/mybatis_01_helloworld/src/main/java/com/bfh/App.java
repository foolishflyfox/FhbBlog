package com.bfh;

import com.bfh.mybatis.bean.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author benfeihu
 */
//@Slf4j
public class App {
    public static void main(String[] args) throws IOException {
        // 访问 mybatis 读取 student 数据
        // 定义 mybatis 主配置文件的名称，从类路径的根开始
        String config = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(config);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        SqlSession sqlSession = factory.openSession();
        String sqlId = "com.bfh.dao.StudentDao" + "." + "selectStudents";
        List<Student> students = sqlSession.selectList(sqlId);
        students.forEach(System.out::println);
        sqlSession.close();
    }

}
