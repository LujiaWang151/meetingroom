package com.meetingroom.dao;
import com.meetingroom.util.DruidUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BaseDao {
    /*
    public Connection getConnection() throws Exception {
        return DruidUtil.getConnection();
    }*/

    protected int update(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DruidUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("SQL执行失败: " + sql, e);
        } finally {
            close(conn, ps, null);
        }
    }

    // 封装关闭资源的方法，Dao直接调用
    public void close(Connection conn, PreparedStatement ps, ResultSet rs){
        try{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
            if(conn!=null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

