package com.meetingroom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface BaseDao {
    int update(String sql, Object... params);
    void close(Connection conn, PreparedStatement ps, ResultSet rs);
}
