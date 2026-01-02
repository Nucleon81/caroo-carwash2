package com.practice.project_1.service;

import com.practice.project_1.model.Booking;
import com.practice.project_1.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlotService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<LocalDateTime> getAvailableSlots(String serviceId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        // Fetch all bookings for the day (for simplicity; optimize if needed)
        List<Booking> dayBookings = bookingRepository.findByScheduledDateTimeBetween(startOfDay, endOfDay);

        // Generate possible slots (e.g., every 30 mins from 10:00 to 20:00)
        List<LocalDateTime> possibleSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(10, 0); // 10:00 AM
        LocalTime endTime = LocalTime.of(20, 0);  // 8:00 PM
        LocalDateTime current = date.atTime(startTime);

        while (current.isBefore(date.atTime(endTime))) {
            possibleSlots.add(current);
            current = current.plusMinutes(30); // 30-min slots
        }

        // Remove booked slots
        possibleSlots.removeIf(slot -> dayBookings.stream()
                .anyMatch(booking -> booking.getScheduledDateTime().equals(slot)));

        return possibleSlots;
    }
}
