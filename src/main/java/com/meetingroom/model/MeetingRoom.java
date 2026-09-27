package com.meetingroom.model;

public class MeetingRoom {
    private Integer id;
    private String room_name;
    private Integer capacity;
    private String location;
    private Boolean isActive;

    public MeetingRoom(){}
    public MeetingRoom(Integer id, String room_name, Integer capacity, String location, Boolean isActive){
        this.id = id;
        this.room_name = room_name;
        this.capacity = capacity;
        this.location = location;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "id=" + id +
                ", name='" + room_name + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
