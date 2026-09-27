package com.meetingroom.dao;

import com.meetingroom.model.Booking;
import com.meetingroom.enums.BookingStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.meetingroom.util.DruidUtil.getConnection;

public class BookingDao extends BaseDao {

    /**
     * 查询全部预约
     */
    private List<Booking> queryList(String sql, Object... params) {
        List<Booking> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setRoomId(rs.getInt("room_id"));
                booking.setPersonName(rs.getString("person_name"));
                booking.setTitle(rs.getString("title"));
                booking.setParticipantCount(rs.getInt("participantCount"));
                booking.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                booking.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
                booking.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                booking.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
                list.add(booking);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询Booking失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    /**
     * 按预约人查询
     */
    public List<Booking> findByPersonName(String personName) {
        String sql = "SELECT booking.*, meeting_room.room_name AS roomName " +
                "FROM booking " +
                "LEFT JOIN meeting_room ON booking.room_id = meeting_room.id " +
                "WHERE booking.person_name = ? " +
                "ORDER BY booking.create_time DESC";
        return queryList(sql, personName);
    }

    /**
     * 按ID查询
     */
    public Booking findById(Integer id) {
        String sql = "SELECT * FROM booking WHERE id = ?";
        List<Booking> list = queryList(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 时间冲突检测：同一会议室、时间段重叠、且状态不是已取消
     */
    public List<Booking> findConflicts(Integer roomId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM booking " +
                "WHERE room_id = ? " +
                "AND status != 'CANCELLED' " +
                "AND start_time < ? " +
                "AND end_time > ?";
        return queryList(sql, roomId, endTime, startTime);
    }

    /**
     * 按状态查询
     */
    public List<Booking> findByStatus(BookingStatus status) {
        String sql = "SELECT booking.*, meeting_room.room_name AS roomName " +
                "FROM booking " +
                "LEFT JOIN meeting_room ON booking.room_id = meeting_room.id " +
                "WHERE booking.status = ? " +
                "ORDER BY booking.create_time DESC";
        return queryList(sql, status.name());
    }

    /**
     * 插入预约
     */
    public int insert(Booking booking) {
        String sql = "INSERT INTO booking (room_id, person_name, title, participantCount, " +
                "start_time, end_time, status, create_time, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return update(sql, booking.getRoomId(), booking.getPersonName(), booking.getTitle(),
                booking.getParticipantCount(), booking.getStartTime(), booking.getEndTime(),
                booking.getStatus().name(), booking.getCreateTime(), booking.getUpdateTime());
    }

    /**
     * 更新预约
     */
    public int update(Booking booking) {
        String sql = "UPDATE booking SET room_id=?, person_name=?, title=?, participantCount=?, " +
                "start_time=?, end_time=?, status=?, update_time=? WHERE id=?";
        return update(sql, booking.getRoomId(), booking.getPersonName(), booking.getTitle(),
                booking.getParticipantCount(), booking.getStartTime(), booking.getEndTime(),
                booking.getStatus().name(), booking.getUpdateTime(), booking.getId());
    }


}