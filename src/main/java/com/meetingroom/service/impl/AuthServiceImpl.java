package com.meetingroom.service.impl;

import com.meetingroom.dao.impl.UserDaoImpl;
import com.meetingroom.model.User;
import com.meetingroom.enums.Role;
import com.meetingroom.service.AuthService;

public class AuthServiceImpl implements AuthService {

    private final UserDaoImpl userDao = new UserDaoImpl();

    /**
     * 登录认证
     * @return 登录成功返回 User 对象，失败返回 null
     */
    @Override
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
    @Override
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    /**
     * 判断是否为员工
     */
    @Override
    public boolean isEmployee(User user) {
        return user != null && user.getRole() == Role.EMPLOYEE;
    }


    /**
    * 按照员工名查询员工信息
    * */
    @Override
    public User findByname(String name){
        User user = userDao.findByUsername(name);
        return user;
    }
}