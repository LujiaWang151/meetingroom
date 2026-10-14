package com.meetingroom.service;

import com.meetingroom.model.Booking;
import com.meetingroom.model.MeetingRoom;
import com.meetingroom.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<MeetingRoom> listAvailableRooms();

    List<Booking> listMyBookings(String username);

    void submitBooking(User user, Integer roomId, String title,
                       LocalDateTime startTime, LocalDateTime endTime,
                       int participantCount);

    void cancelBooking(User user, Integer bookingId);

    List<Booking> listPendingBookings();

    void approveBooking(Integer bookingId);

    void rejectBooking(Integer bookingId);
}
