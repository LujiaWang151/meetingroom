package com.meetingroom.service;

import com.meetingroom.model.MeetingRoom;
import com.meetingroom.model.User;

import java.util.List;

public interface MeetingRoomService {
    List<MeetingRoom> listAllRooms();

    MeetingRoom listIdRoom(Integer id);

    void updateRoom(User user, Integer roomId, String name, Integer capacity, String location, Boolean isActive);

    void addRoom(User user, String name, Integer capacity, String location, Boolean isActive);

    void deleteRoom(User user, Integer roomId);
}
