package com.meetingroom.dao;

import com.meetingroom.model.User;
import com.meetingroom.enums.Role;
import com.meetingroom.util.DruidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends BaseDao {

    /**
     * 按用户名查询用户
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DruidUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                // 数据库存的是字符串，转成枚举
                user.setRole(Role.valueOf(rs.getString("role")));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            close(conn,ps,rs);
        }
        return null;
    }
}
