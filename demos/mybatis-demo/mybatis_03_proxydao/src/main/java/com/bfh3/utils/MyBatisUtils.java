package com.bfh3.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * @author benfeihu
 */
public class MyBatisUtils {

    static SqlSessionFactory factory;
    static String config = "mybatis-config.xml";

    static {
        try {
            InputStream in = Resources.getResourceAsStream(config);
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            factory = builder.build(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSqlSession() {
        return getSqlSession(false);
    }
    public static SqlSession getSqlSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }
}
