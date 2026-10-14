package com.meetingroom.dao;

import com.meetingroom.model.User;

public interface UserDao {
    User findByUsername(String username);
}
