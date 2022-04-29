package com.bfh2.dao.impl;

import com.bfh2.bean.Student;
import com.bfh2.dao.StudentDao;
import com.bfh2.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author benfeihu
 */
public class StudentDaoImpl implements StudentDao {
    @Override
    public List<Student> selectStudents() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        String sqlId = "com.bfh2.dao.StudentDao" + "." + "selectStudents";
        List<Student> students = sqlSession.selectList(sqlId);
        sqlSession.close();
        return students;
    }

    @Override
    public int insertStudent(Student student) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        String sqlId = "com.bfh2.dao.StudentDao" + "." + "insertStudent";
        int insertCount = sqlSession.insert(sqlId, student);
        sqlSession.commit();
        sqlSession.close();
        return insertCount;
    }
}
