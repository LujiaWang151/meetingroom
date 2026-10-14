package com.meetingroom.dao;

import com.meetingroom.model.MeetingRoom;

import java.util.List;

public interface MeetingRoomDao {

    List<MeetingRoom> queryMeetingRooms(String sql, Object... params);

    List<MeetingRoom> findAll();

    MeetingRoom findById(Integer id);

    int update(MeetingRoom room);

    int insert(MeetingRoom room);

    int deleteById(Integer id);
}
