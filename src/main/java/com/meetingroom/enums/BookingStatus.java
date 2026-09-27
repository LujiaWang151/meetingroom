package com.meetingroom.enums;

public enum BookingStatus {
    PENDING("待审批"),
    APPROVED("已通过"),
    REJECTED("已拒绝"),
    CANCELLED("已取消");

    private final String description;
    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
