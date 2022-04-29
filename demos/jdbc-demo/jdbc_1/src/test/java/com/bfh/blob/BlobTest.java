package com.bfh.blob;

import com.bfh.bean.Customer;
import com.bfh.util.JDBCUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * @author benfeihu
 * 使用 PreparedStatement 操作 blob 字段
 */
public class BlobTest {
    // 向数据表 customers 中插入 Blob 类型字段
    @Test
    public void testInert() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name, email, birth, photo) values(?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "xx");
        ps.setObject(2, "xx@163.com");
        ps.setObject(3, "1992-1-1");
//        FileInputStream is = new FileInputStream(new File());
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("xx.png");
        ps.setBlob(4, is);
        ps.execute();
        JDBCUtils.closeResource(conn, ps);
    }

    @Test
    public void testQuery() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth,photo from customers where id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, 25);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
//            方式一
//            int id = rs.getInt(1);
//            String name = rs.getString(2);
//            String email = rs.getString(3);
//            Date birth = rs.getDate(4);
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");


            Customer cust = new Customer(id, name, email, birth);
            System.out.println(cust);

            Blob photo = rs.getBlob("photo");
            // 将 blob 类型的字段下载下来保持在本地
            InputStream is = photo.getBinaryStream();
            FileOutputStream fos = new FileOutputStream("abc.png");
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer))!=-1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
        }
        JDBCUtils.closeResource(conn, ps);
    }
}
