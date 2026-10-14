package com.meetingroom.dao;

import com.meetingroom.enums.BookingStatus;
import com.meetingroom.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDao {
    List<Booking> queryList(String sql, Object... params);

    List<Booking> findByPersonName(String personName);

    Booking findById(Integer id);

    List<Booking> findConflicts(Integer roomId, LocalDateTime startTime, LocalDateTime endTime);

    List<Booking> findByStatus(BookingStatus status);

    int insert(Booking booking);

    int update(Booking booking);
}
