package com.meetingroom.service;

import com.meetingroom.model.User;

public interface AuthService {
    User login(String username, String password);

    boolean isAdmin(User user);

    boolean isEmployee(User user);

    User findByname(String name);
}
