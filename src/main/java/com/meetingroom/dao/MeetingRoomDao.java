package com.meetingroom.dao;

import com.meetingroom.model.MeetingRoom;
import com.meetingroom.util.DruidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeetingRoomDao extends BaseDao {

    protected List<MeetingRoom> queryMeetingRooms(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DruidUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            List<MeetingRoom> list = new ArrayList<>();
            while (rs.next()) {
                MeetingRoom room = new MeetingRoom();
                room.setId(rs.getInt("id"));
                room.setRoom_name(rs.getString("room_name"));
                room.setCapacity(rs.getInt("capacity"));
                room.setLocation(rs.getString("location"));
                room.setIsActive(rs.getBoolean("is_active"));
                list.add(room);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("查询失败: " + sql, e);
        } finally {
            close(conn, ps, rs);
        }
    }

    /**
     * 查询所有会议室
     */
    public List<MeetingRoom> findAll() {
        String sql = "SELECT * FROM meeting_room";
        return queryMeetingRooms(sql);
    }

    /**
     * 按ID查询会议室
     */
    public MeetingRoom findById(Integer id) {
        String sql = "SELECT * FROM meeting_room WHERE id = ?";
        List<MeetingRoom> list = queryMeetingRooms(sql, id);
        return list.isEmpty() ? null : list.get(0);

    }

    /**
     * 更新会议室信息
     */
    public int update(MeetingRoom room) {
        String sql = "UPDATE meeting_room SET room_name=?, capacity=?, location=?, is_active=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DruidUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoom_name());
            ps.setInt(2, room.getCapacity());
            ps.setString(3, room.getLocation());
            ps.setBoolean(4, room.getIsActive());
            ps.setInt(5, room.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn,ps,null);
        }
        return 0;
    }

    /**
     * 新增会议室
     */
    public int insert(MeetingRoom room) {
        String sql = "INSERT INTO meeting_room(room_name, capacity, location, is_active) VALUES(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DruidUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoom_name());
            ps.setInt(2, room.getCapacity());
            ps.setString(3, room.getLocation());
            ps.setBoolean(4, room.getIsActive());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn,ps,null);
        }
        return 0;
    }

    /**
     * 删除会议室
     */
    public int deleteById(Integer id) {
        String sql = "DELETE FROM meeting_room WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DruidUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn,ps,null);
        }
        return 0;
    }
}
