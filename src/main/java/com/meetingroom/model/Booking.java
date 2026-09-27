package com.meetingroom.model;

import com.meetingroom.enums.BookingStatus;

import java.time.LocalDateTime;

public class Booking {
    private Integer id;
    private Integer room_id;
    private String person_name;
    private String title;
    private Integer participantCount;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private BookingStatus status;
    private LocalDateTime create_time;
    private LocalDateTime update_time;

    private String roomName;

    public Booking() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return room_id;
    }

    public void setRoomId(Integer roomId) {
        this.room_id = roomId;
    }

    public String getPersonName() {
        return person_name;
    }

    public void setPersonName(String personName) {
        this.person_name = personName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    public LocalDateTime getStartTime() {
        return start_time;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.start_time = startTime;
    }

    public LocalDateTime getEndTime() {
        return end_time;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.end_time = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return create_time;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.create_time = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.update_time = updateTime;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", roomId=" + room_id +
                ", roomName='" + roomName + '\'' +
                ", personName='" + person_name + '\'' +
                ", title='" + title + '\'' +
                ", participantCount=" + participantCount +
                ", startTime=" + start_time +
                ", endTime=" + end_time +
                ", status=" + status +
                '}';
    }

}
