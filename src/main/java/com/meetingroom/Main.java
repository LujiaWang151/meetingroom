package com.meetingroom;

import com.meetingroom.enums.BookingStatus;
import com.meetingroom.model.Booking;
import com.meetingroom.model.MeetingRoom;
import com.meetingroom.model.User;
import com.meetingroom.service.impl.AuthServiceImpl;
import com.meetingroom.service.impl.BookingServiceImpl;
import com.meetingroom.service.impl.MeetingRoomServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthServiceImpl AUTH_SERVICE_IMPL = new AuthServiceImpl();
    private static final BookingServiceImpl BOOKING_SERVICE_IMPL = new BookingServiceImpl();
    private static final MeetingRoomServiceImpl MEETING_ROOM_SERVICE_IMPL = new MeetingRoomServiceImpl();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("       会议室预约系统 v1.0");
        System.out.println("================================");

        // 1. 登录
        login();

        // 2. 根据角色进入对应菜单
        if (currentUser != null) {
            if (AUTH_SERVICE_IMPL.isAdmin(currentUser)) {
                adminMenu();
            } else if (AUTH_SERVICE_IMPL.isEmployee(currentUser)) {
                employeeMenu();
            }
        }

        System.out.println("\n已退出系统，再见！");
        scanner.close();
    }

    // ==================== 登录 ====================

    /**
     * 登录：循环提示输入用户名密码，直到成功
     */
    private static void login() {
        while (true) {
            System.out.println("\n--- 用户登录 ---");
            System.out.print("请输入用户名: ");
            String username = scanner.nextLine().trim();
            System.out.print("请输入密码: ");
            String password = scanner.nextLine().trim();

            currentUser = AUTH_SERVICE_IMPL.login(username, password);
            if (currentUser != null) {
                break; // 登录成功，跳出循环
            }

            System.out.print("登录失败，是否重新登录？(y/n): ");
            if (!"y".equalsIgnoreCase(scanner.nextLine().trim())) {
                System.out.println("程序退出");
                System.exit(0);
            }
        }
    }

    // ==================== 员工菜单 ====================

    private static void employeeMenu() {
        while (true) {
            System.out.println("\n================================");
            System.out.println("欢迎, " + currentUser.getUsername() + " (员工)");
            System.out.println("================================");
            System.out.println("1. 查看所有可用会议室");
            System.out.println("2. 查看我的预约记录");
            System.out.println("3. 提交预约申请");
            System.out.println("4. 取消预约");
            System.out.println("0. 退出登录");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listAvailableRooms();
                    break;
                case "2":
                    listMyBookings();
                    break;
                case "3":
                    submitBooking();
                    break;
                case "4":
                    cancelBooking();
                    break;
                case "0":
                    currentUser = null;
                    login();
                    // 重新登录后根据角色进入对应菜单
                    if (AUTH_SERVICE_IMPL.isAdmin(currentUser)) {
                        adminMenu();
                    } else {
                        employeeMenu();
                    }
                    return; // 注意这里要 return，否则会继续执行下面的代码
                default:
                    System.out.println("输入无效，请重新选择");
            }
        }
    }

    // ==================== 管理员菜单 ====================

    private static void adminMenu() {
        while (true) {
            System.out.println("\n================================");
            System.out.println("欢迎, " + currentUser.getUsername() + " (管理员)");
            System.out.println("================================");
            System.out.println("1. 查看所有会议室");
            System.out.println("2. 按id查询会议室");
            System.out.println("3. 查看待审批预约");
            System.out.println("4. 审批通过预约");
            System.out.println("5. 拒绝预约");
            System.out.println("6. 新增会议室");
            System.out.println("7. 修改会议室");
            System.out.println("8. 删除会议室");
            System.out.println("0. 退出登录");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listAllRooms();
                    break;
                case "2":
                    System.out.print("请输入您要查询的会议室id号:");
                    Integer id = scanner.nextInt();
                    listIdRoom(id);
                    break;
                case "3":
                    listPendingBookings();
                    break;
                case "4":
                    approveBooking();
                    break;
                case "5":
                    rejectBooking();
                    break;
                case "6":
                    addMeetingRoom();
                    break;
                case "7":
                    updateMeetingRoom();
                    break;
                case "8":
                    deleteMeetingRoom();
                    break;
                case "0":
                    currentUser = null;
                    login();
                    if (AUTH_SERVICE_IMPL.isAdmin(currentUser)) {
                        adminMenu();
                    } else {
                        employeeMenu();
                    }
                    return;
                default:
                    System.out.println("输入无效，请重新选择");
            }
        }
    }


    // ==================== 员工功能实现 ====================

    /**
     * 查看所有可用会议室
     */
    private static void listAvailableRooms() {
        List<MeetingRoom> rooms = BOOKING_SERVICE_IMPL.listAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("暂无可用会议室");
        } else {
            System.out.println("\n--- 可用会议室列表 ---");
            for (MeetingRoom room : rooms) {
                System.out.println(room.getId() + ". " + room.getRoom_name()
                        + " | 容量: " + room.getCapacity() + "人 | 位置: " + room.getLocation()
                        + " | 状态: " + (room.getIsActive() ? "启用" : "停用"));
            }
        }
        waitInput();
    }

    /**
     * 查看我的预约记录
     */
    private static void listMyBookings() {
        List<Booking> bookings = BOOKING_SERVICE_IMPL.listMyBookings(currentUser.getUsername());
        if (bookings.isEmpty()) {
            System.out.println("你暂无预约记录");
        } else {
            System.out.println("\n--- 我的预约记录 ---");
            for (Booking b : bookings) {
                System.out.println("编号: " + b.getId()
                        + " | 会议室: " + b.getRoomId()
                        + " | 标题: " + b.getTitle()
                        + " | 参会人数: " + b.getParticipantCount()
                        + " | 开始: " + b.getStartTime()
                        + " | 结束: " + b.getEndTime()
                        + " | 状态: " + b.getStatus().getDescription());
            }
        }
        waitInput();
    }

    /**
     * 提交预约申请
     */
    private static void submitBooking() {
        // 先显示可用会议室让用户选
        List<MeetingRoom> rooms = BOOKING_SERVICE_IMPL.listAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("暂无可用会议室，请联系管理员");
            waitInput();
            return;
        }

        System.out.println("\n--- 可用会议室 ---");
        for (MeetingRoom room : rooms) {
            System.out.println(room.getId() + ". " + room.getRoom_name()
                    + " | 容量: " + room.getCapacity() + "人 | 位置: " + room.getLocation());
        }

        // 获取会议室ID
        Integer roomId = null;
        while (roomId == null) {
            System.out.print("\n请选择会议室编号: ");
            String input = scanner.nextLine().trim();
            try {
                roomId = Integer.parseInt(input);
                if(MEETING_ROOM_SERVICE_IMPL.listIdRoom(roomId)==null){
                    System.out.println("该会议室不存在，请重新输入!");
                    roomId = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字");
            }
        }

        // 获取标题
        String title = "";
        while (title.isEmpty()) {
            System.out.print("请输入预约标题: ");
            title = scanner.nextLine().trim();
            if(title.isEmpty())
                System.out.println("标题不能为空,请重新输入！");
        }

        // 获取参会人数
        int participantCount = 0;
        while (participantCount <= 0) {
            System.out.print("请输入参会人数: ");
            String input = scanner.nextLine().trim();
            try {
                participantCount = Integer.parseInt(input);
                if (participantCount <= 0) {
                    System.out.println("人数必须大于0，请重新输入！");
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字");
            }
        }

        // 获取开始时间
        LocalDateTime startTime = null;
        while (startTime == null) {
            System.out.print("请输入开始时间 (格式: yyyy-MM-dd HH:mm, 如 2026-09-25 10:00): ");
            String input = scanner.nextLine().trim();
            try {
                startTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if(startTime.isBefore(LocalDateTime.now())){
                    System.out.println("预约开始时间不能是过去时间，请重新输入！");
                    startTime = null; // 置空，继续循环让用户重输
                }
            } catch (DateTimeParseException e) {
                System.out.println("时间格式错误，请重新输入！");
            }
        }

        // 获取结束时间
        LocalDateTime endTime = null;
        while (endTime == null) {
            System.out.print("请输入结束时间 (格式: yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine().trim();
            try {
                endTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (!endTime.isAfter(startTime)) {
                    System.out.println("结束时间必须晚于开始时间，请重新输入！");
                    endTime = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("时间格式错误，请重新输入！");
            }
        }

        // 调用服务层提交
        BOOKING_SERVICE_IMPL.submitBooking(currentUser, roomId, title, startTime, endTime, participantCount);
        waitInput();
    }

    /**
     * 取消预约
     */
    private static void cancelBooking() {
        List<Booking> bookings = BOOKING_SERVICE_IMPL.listMyBookings(currentUser.getUsername());
        if (bookings.isEmpty()) {
            System.out.println("你暂无预约记录，无法取消");
            waitInput();
            return;
        }

        System.out.println("\n--- 我的预约(可取消) ---");
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.PENDING && b.getStartTime().isAfter(LocalDateTime.now())) {
                System.out.println(b.getId() + ". " + b.getTitle()
                        + " | 会议室: " + b.getRoomId()
                        + " | 时间: " + b.getStartTime() + " ~ " + b.getEndTime()
                        + " | 状态: " + b.getStatus().getDescription());
            }
        }

        System.out.print("\n请输入要取消的预约编号: ");
        String input = scanner.nextLine().trim();
        Integer bookingId = null;
        try {
            bookingId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        BOOKING_SERVICE_IMPL.cancelBooking(currentUser, bookingId);
        waitInput();
    }

    // ==================== 管理员功能实现 ====================

    /**
     * 查看所有会议室
     */
    private static void listAllRooms() {
        List<MeetingRoom> rooms = MEETING_ROOM_SERVICE_IMPL.listAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("暂无会议室");
        } else {
            System.out.println("\n--- 会议室列表 ---");
            for (MeetingRoom room : rooms) {
                System.out.println(room.getId() + ". " + room.getRoom_name()
                        + " | 容量: " + room.getCapacity() + "人 | 位置: " + room.getLocation()
                        + " | 状态: " + (room.getIsActive() ? "启用" : "停用"));
            }
        }
        waitInput();
    }

    private static void listIdRoom(Integer id) {

        MeetingRoom room = MEETING_ROOM_SERVICE_IMPL.listIdRoom(id);
        if (room != null) {
            System.out.println("\n--- 查询结果 ---");
            System.out.println("会议室ID: " + room.getId());
            System.out.println("会议室名称: " + room.getRoom_name());
            System.out.println("容量: " + room.getCapacity() + "人");
            System.out.println("位置: " + room.getLocation());
            System.out.println("状态: " + (room.getIsActive() ? "启用" : "停用"));
        } else {
            System.out.println("未找到该ID的会议室");
        }
        waitInput();
    }

    /**
     * 查看待审批预约
     */
    private static void listPendingBookings() {
        List<Booking> bookings = BOOKING_SERVICE_IMPL.listPendingBookings();
        if (bookings.isEmpty()) {
            System.out.println("暂无待审批预约");
        } else {
            System.out.println("\n--- 待审批预约列表 ---");
            for (Booking b : bookings) {
                System.out.println("编号: " + b.getId()
                        + " | 预约人: " + b.getPersonName()
                        + " | 会议室: " + b.getRoomId()
                        + " | 标题: " + b.getTitle()
                        + " | 参会人数: " + b.getParticipantCount()
                        + " | 开始: " + b.getStartTime()
                        + " | 结束: " + b.getEndTime());
            }
        }
        waitInput();
    }

    /**
     * 审批通过预约
     */
    private static void approveBooking() {
        listPendingBookings(); // 先显示待审批列表
        System.out.print("\n请输入要审批通过的预约编号: ");
        String input = scanner.nextLine().trim();
        Integer bookingId = null;
        try {
            bookingId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        BOOKING_SERVICE_IMPL.approveBooking(bookingId);
        waitInput();
    }

    /**
     * 拒绝预约
     */
    private static void rejectBooking() {
        listPendingBookings(); // 先显示待审批列表
        System.out.print("\n请输入要拒绝的预约编号: ");
        String input = scanner.nextLine().trim();
        Integer bookingId = null;
        try {
            bookingId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        BOOKING_SERVICE_IMPL.rejectBooking(bookingId);
        waitInput();
    }

    /**
     * 新增会议室
     */
    private static void addMeetingRoom() {

        System.out.print("请输入会议室名称: ");
        String name = scanner.nextLine().trim();
        System.out.print("请输入容量: ");
        Integer capacity = null;
        try {
            capacity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        System.out.print("请输入位置: ");
        String location = scanner.nextLine().trim();

        MeetingRoom room = new MeetingRoom();
        room.setRoom_name(name);
        room.setCapacity(capacity);
        room.setLocation(location);
        room.setIsActive(true);

        MEETING_ROOM_SERVICE_IMPL.addRoom(currentUser, name, capacity, location, true);

        waitInput();
    }

    /**
     * 修改会议室
     */
    private static void updateMeetingRoom() {
        System.out.print("请输入要修改的会议室编号: ");
        String input = scanner.nextLine().trim();
        Integer id = null;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        // 先查出会议室
        MeetingRoom room = MEETING_ROOM_SERVICE_IMPL.listIdRoom(id);
        if (room == null) {
            System.out.println("会议室不存在");
            waitInput();
            return;
        }
        System.out.println("当前名称: " + room.getRoom_name());
        System.out.print("请输入新名称: ");
        room.setRoom_name(scanner.nextLine().trim());
        System.out.print("请输入新容量: ");
        try {
            room.setCapacity(Integer.parseInt(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        System.out.print("请输入新位置: ");
        room.setLocation(scanner.nextLine().trim());

        MeetingRoomServiceImpl service = new MeetingRoomServiceImpl();
        service.updateRoom(currentUser, id, room.getRoom_name(), room.getCapacity(), room.getLocation(), true);
        waitInput();
    }

    /**
     * 删除会议室
     */
    private static void deleteMeetingRoom() {
        System.out.print("请输入要删除的会议室编号: ");
        String input = scanner.nextLine().trim();
        Integer id = null;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
            waitInput();
            return;
        }
        MEETING_ROOM_SERVICE_IMPL.deleteRoom(currentUser,id);
        waitInput();
    }

    /**
     * 暂停，等用户按键继续
     */
    private static void waitInput() {
        System.out.print("\n按回车键继续...");
        scanner.nextLine();
    }
}