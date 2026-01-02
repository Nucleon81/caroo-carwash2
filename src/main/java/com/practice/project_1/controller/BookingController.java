package com.practice.project_1.controller;

import com.practice.project_1.dto.BookingRequestDTO;
import com.practice.project_1.dto.ReviewRequest;
import com.practice.project_1.model.AddOn;
import com.practice.project_1.model.Booking;
import com.practice.project_1.service.AddOnService;
import com.practice.project_1.service.BookingService;
import com.practice.project_1.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired private BookingService bookingService;

    @Autowired private AddOnService addOnService;

    @Autowired private SlotService slotService;


    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody BookingRequestDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        Booking booking = bookingService.createBooking(dto, authHeader);
        booking.setStatus("PENDING");
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getUserBookings(
            @RequestHeader("Authorization") String authHeader) {
        List<Booking> bookings = bookingService.getBookingsForUser(authHeader);
        return ResponseEntity.ok(bookings);
    }

    // New: Get available slots for a service and date
    @GetMapping("/slots")
    public ResponseEntity<List<LocalDateTime>> getAvailableSlots(
            @RequestParam String serviceId,
            @RequestParam String date) {
        List<LocalDateTime> slots = slotService.getAvailableSlots(serviceId, LocalDate.parse(date));
        return ResponseEntity.ok(slots);
    }

    // New: Get all add-ons for selection
    @GetMapping("/addons")
    public ResponseEntity<List<AddOn>> getAllAddOns() {
        List<AddOn> addOns = addOnService.getAllAddOns();
        return ResponseEntity.ok(addOns);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<String> addReview(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ReviewRequest request) {
        bookingService.addReview(id, authHeader, request);
        return ResponseEntity.ok("Review added successfully");
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelBooking(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        bookingService.cancelBooking(id, authHeader);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> rescheduleBooking(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDateTime,
            @RequestHeader("Authorization") String authHeader) {
        bookingService.rescheduleBooking(id, newDateTime, authHeader);
        return ResponseEntity.ok("Booking rescheduled successfully");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateBookingStatus(
            @PathVariable String id,
            @RequestParam String status,  // e.g., "COMPLETED"
            @RequestHeader("Authorization") String authHeader) {
        // Optional: Add role check for admin (e.g., hasRole("ADMIN"))
        bookingService.updateBookingStatus(id, status, authHeader);
        return ResponseEntity.ok("Status updated to " + status);
    }

    // In BookingController.java

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        Booking booking = bookingService.getBookingByIdForUser(id, authHeader);
        return ResponseEntity.ok(booking);
    }


}
