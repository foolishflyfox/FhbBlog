package com.bfh.dao;

import com.bfh.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author benfeihu
 * DAO: Data Access Object
 * 封装了针对数据表的通用操作
 */
public class BaseDAO {

    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        int r = 0;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i+1, args[i]);
            r = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
        return r;
    }

    // 通用的查询操作，用于返回一条数据表中的一条记录(version2.0 考虑上事务)
    public <T> List<T> getInstance(Connection conn, String sql, Class<T> clazz, Object... args) {
        List<T> result = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) ps.setObject(i + 1, args[i]);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCnt = rsmd.getColumnCount();
            while (rs.next()) {
                T tmp = clazz.newInstance();
                for (int i = 1; i <= columnCnt; ++i) {
                    String columnName = rsmd.getColumnLabel(i);
                    Object columnValue = rs.getObject(i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(tmp, columnValue);
                }
                result.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return result;
    }

    public <E> E getValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
