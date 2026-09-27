package com.meetingroom.enums;

public enum Role {
    EMPLOYEE("员工"),
    ADMIN("管理员");

    private final String description;
    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
