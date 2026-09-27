package com.meetingroom.service;

import com.meetingroom.dao.BookingDao;
import com.meetingroom.dao.MeetingRoomDao;
import com.meetingroom.model.Booking;
import com.meetingroom.model.MeetingRoom;
import com.meetingroom.model.User;
import com.meetingroom.enums.BookingStatus;
import com.meetingroom.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingDao bookingDao = new BookingDao();
    private final MeetingRoomDao meetingRoomDao = new MeetingRoomDao();

    // ==================== 员工功能 ====================

    /**
     * 查询所有可用会议室
     */
    public List<MeetingRoom> listAvailableRooms() {
        List<MeetingRoom> rooms = meetingRoomDao.findAll();
        rooms.removeIf(room -> !room.getIsActive()); // 过滤停用的会议室
        return rooms;
    }

    /**
     * 查询自己的预约记录
     */
    public List<Booking> listMyBookings(String username) {
        return bookingDao.findByPersonName(username);
    }

    /**
     * 提交预约申请
     */
    public boolean submitBooking(User user, Integer roomId, String title,
                                 LocalDateTime startTime, LocalDateTime endTime,
                                 int participantCount) {
        // 1. 校验角色
        if (user.getRole() != Role.EMPLOYEE) {
            System.out.println("只有员工可以提交预约");
            return false;
        }

        // 2. 校验时间：开始时间必须早于结束时间
        if (!startTime.isBefore(endTime)) {
            System.out.println("开始时间必须早于结束时间");
            return false;
        }

        // 3. 校验会议室是否存在且启用
        MeetingRoom room = meetingRoomDao.findById(roomId);
        if (room == null || !room.getIsActive()) {
            System.out.println("会议室不存在或已停用");
            return false;
        }

        // 4. 校验参会人数不超过会议室容量
        if (participantCount > room.getCapacity()) {
            System.out.println("参会人数(" + participantCount + ")超过会议室容量(" + room.getCapacity() + ")");
            return false;
        }

        // 5. 检测时间冲突（同一会议室、时间段重叠、且状态不是已取消）
        List<Booking> conflicts = bookingDao.findConflicts(roomId, startTime, endTime);
        if (!conflicts.isEmpty()) {
            System.out.println("该时段已有预约，请选择其他时间");
            return false;
        }

        // 6. 构建预约对象并提交
        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setPersonName(user.getUsername());
        booking.setTitle(title);
        booking.setParticipantCount(participantCount);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreateTime(LocalDateTime.now()); // 自动记录提交时间
        booking.setUpdateTime(LocalDateTime.now());

        int rows = bookingDao.insert(booking);
        if (rows > 0) {
            System.out.println("预约申请提交成功，等待审批");
            return true;
        } else {
            System.out.println("预约提交失败");
            return false;
        }
    }

    /**
     * 取消预约（只能取消待审批且未开始的）
     */
    public boolean cancelBooking(User user, Integer bookingId) {
        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) {
            System.out.println("预约不存在");
            return false;
        }

        // 只能取消自己的预约
        if (!booking.getPersonName().equals(user.getUsername())) {
            System.out.println("只能取消自己的预约");
            return false;
        }

        // 只能取消待审批状态的
        if (booking.getStatus() != BookingStatus.PENDING) {
            System.out.println("只能取消待审批状态的预约，当前状态：" + booking.getStatus().getDescription());
            return false;
        }

        // 只能取消未开始的
        if (booking.getStartTime().isBefore(LocalDateTime.now()) || booking.getStartTime().isEqual(LocalDateTime.now())) {
            System.out.println("预约已开始或已过，无法取消");
            return false;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdateTime(LocalDateTime.now());
        int rows = bookingDao.update(booking);
        if (rows > 0) {
            System.out.println("预约已取消");
            return true;
        } else {
            System.out.println("取消失败");
            return false;
        }
    }

    // ==================== 管理员功能 ====================

    /**
     * 查询待审批预约列表
     */
    public List<Booking> listPendingBookings() {
        return bookingDao.findByStatus(BookingStatus.PENDING);
    }

    /**
     * 审批通过
     */
    public boolean approveBooking(Integer bookingId) {
        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) {
            System.out.println("预约不存在");
            return false;
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            System.out.println("只能审批待审批状态的预约");
            return false;
        }

        booking.setStatus(BookingStatus.APPROVED);
        booking.setUpdateTime(LocalDateTime.now());
        int rows = bookingDao.update(booking);
        if (rows > 0) {
            System.out.println("预约已通过审批");
            return true;
        } else {
            System.out.println("审批失败");
            return false;
        }
    }

    /**
     * 拒绝预约
     */
    public boolean rejectBooking(Integer bookingId) {
        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) {
            System.out.println("预约不存在");
            return false;
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            System.out.println("只能审批待审批状态的预约");
            return false;
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setUpdateTime(LocalDateTime.now());
        int rows = bookingDao.update(booking);
        if (rows > 0) {
            System.out.println("预约已拒绝");
            return true;
        } else {
            System.out.println("拒绝失败");
            return false;
        }
    }
}