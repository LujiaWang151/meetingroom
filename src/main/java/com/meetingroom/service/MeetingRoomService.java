package com.meetingroom.service;

import com.meetingroom.dao.MeetingRoomDao;
import com.meetingroom.model.MeetingRoom;
import com.meetingroom.model.User;
import com.meetingroom.enums.Role;

import java.util.List;

public class MeetingRoomService {

    private static MeetingRoomDao meetingRoomDao = new MeetingRoomDao();

    /**
     * 查询所有会议室（管理员用，能看到停用的）
     */
    public static List<MeetingRoom> listAllRooms() {
        return meetingRoomDao.findAll();
    }

    public static MeetingRoom listIdRoom(Integer id) {
        return meetingRoomDao.findById(id);
    }
    /**
     * 修改会议室信息
     */
    public boolean updateRoom(User user, Integer roomId, String name, Integer capacity, String location, Boolean isActive) {
        // 校验管理员权限
        if (user.getRole() != Role.ADMIN) {
            System.out.println("只有管理员可以修改会议室信息");
            return false;
        }

        MeetingRoom room = meetingRoomDao.findById(roomId);
        if (room == null) {
            System.out.println("会议室不存在");
            return false;
        }

        if (name != null) room.setRoom_name(name);
        if (capacity != null) room.setCapacity(capacity);
        if (location != null) room.setLocation(location);
        if (isActive != null) room.setIsActive(isActive);

        int rows = meetingRoomDao.update(room);
        if (rows > 0) {
            System.out.println("会议室信息已更新");
            return true;
        } else {
            System.out.println("更新失败");
            return false;
        }
    }

    /**
     * 新增会议室
     */
    public boolean addRoom(User user, String name, Integer capacity, String location, Boolean isActive) {
        if (user.getRole() != Role.ADMIN) {
            System.out.println("只有管理员可以新增会议室");
            return false;
        }

        MeetingRoom room = new MeetingRoom(null, name, capacity, location, isActive);
        int rows = meetingRoomDao.insert(room);
        if (rows > 0) {
            System.out.println("会议室添加成功");
            return true;
        } else {
            System.out.println("添加失败");
            return false;
        }
    }

    /**
     * 删除会议室
     */
    public boolean deleteRoom(User user, Integer roomId) {
        if (user.getRole() != Role.ADMIN) {
            System.out.println("只有管理员可以删除会议室");
            return false;
        }

        int rows = meetingRoomDao.deleteById(roomId);
        if (rows > 0) {
            System.out.println("会议室已删除");
            return true;
        } else {
            System.out.println("删除失败");
            return false;
        }
    }
}
