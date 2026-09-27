package com.meetingroom.service;

import com.meetingroom.dao.UserDao;
import com.meetingroom.model.User;
import com.meetingroom.enums.Role;

public class AuthService {

    private UserDao userDao = new UserDao();

    /**
     * 登录认证
     * @return 登录成功返回 User 对象，失败返回 null
     */
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("用户名和密码不能为空");
            return null;
        }

        User user = userDao.findByUsername(username);
        if (user == null) {
            System.out.println("用户不存在");
            return null;
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("密码错误");
            return null;
        }

        System.out.println("登录成功，欢迎 " + user.getUsername() + "，角色：" + user.getRole().getDescription());
        return user;
    }

    /**
     * 判断是否为管理员
     */
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    /**
     * 判断是否为员工
     */
    public boolean isEmployee(User user) {
        return user != null && user.getRole() == Role.EMPLOYEE;
    }
}