package com.meetingroom.dao.impl;

import com.meetingroom.dao.MeetingRoomDao;
import com.meetingroom.model.MeetingRoom;
import com.meetingroom.util.DruidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MeetingRoomDaoImpl extends BaseDaoImpl implements MeetingRoomDao {

    @Override
    public List<MeetingRoom> queryMeetingRooms(String sql, Object... params) {
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
    @Override
    public List<MeetingRoom> findAll() {
        String sql = "SELECT * FROM meeting_room";
        return queryMeetingRooms(sql);
    }

    /**
     * 按ID查询会议室
     */
    @Override
    public MeetingRoom findById(Integer id) {
        String sql = "SELECT * FROM meeting_room WHERE id = ?";
        List<MeetingRoom> list = queryMeetingRooms(sql, id);
        return list.isEmpty() ? null : list.get(0);

    }

    /**
     * 更新会议室信息
     */
    @Override
    public int update(MeetingRoom room) {
        String sql = "UPDATE meeting_room SET room_name=?, capacity=?, location=?, is_active=? WHERE id=?";
        return update(sql, room.getRoom_name(), room.getCapacity(), room.getLocation(), room.getIsActive(), room.getId());
    }

    /**
     * 新增会议室
     */
    @Override
    public int insert(MeetingRoom room) {
        String sql = "INSERT INTO meeting_room(room_name, capacity, location, is_active) VALUES(?, ?, ?, ?)";
        return update(sql,room.getRoom_name(),room.getCapacity(),room.getLocation(),room.getIsActive());
    }

    /**
     * 删除会议室
     */
    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM meeting_room WHERE id = ?";
        return update(sql,id);
    }
}
