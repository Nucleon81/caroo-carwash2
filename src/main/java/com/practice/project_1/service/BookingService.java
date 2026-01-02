package com.practice.project_1.service;

import com.practice.project_1.dto.BookingRequestDTO;
import com.practice.project_1.dto.ReviewRequest;
import com.practice.project_1.model.AddOn;
import com.practice.project_1.model.Booking;
import com.practice.project_1.model.Service;
import com.practice.project_1.repository.AddOnRepository;
import com.practice.project_1.repository.BookingRepository;
import com.practice.project_1.repository.ServiceRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private AddOnRepository addOnRepository;
    @Autowired private PriceService priceService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public void addReview(String bookingId, String authHeader, ReviewRequest request) {
        String userMobile = extractMobileFromJwt(authHeader);
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty() || !opt.get().getUserMobileNumber().equals(userMobile)) {
            throw new RuntimeException("Booking not found or not owned by user");
        }
        Booking booking = opt.get();
        if (!"COMPLETED".equals(booking.getStatus())) {
            throw new RuntimeException("Can only review completed bookings");
        }
        booking.setReview(request.getReview());
        booking.setRating(request.getRating());
        bookingRepository.save(booking);
    }

    // In BookingService.java

    public Booking getBookingByIdForUser(String bookingId, String authHeader) {
        String userMobile = extractMobileFromJwt(authHeader);
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        Booking booking = opt.get();
        if (!booking.getUserMobileNumber().equals(userMobile)) {
            throw new RuntimeException("You do not own this booking");
        }
        return booking;
    }


    public void updateBookingStatus(String bookingId, String newStatus, String authHeader) {
        String userMobile = extractMobileFromJwt(authHeader);
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        Booking booking = opt.get();

        // Relaxed validation: Allow from "PENDING" or "WASHING"
        if ("COMPLETED".equals(newStatus) && !List.of("PENDING", "WASHING").contains(booking.getStatus())) {
            throw new RuntimeException("Can only complete pending or in-progress bookings");
        }

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
    }




    public void cancelBooking(String bookingId, String authHeader) {
        String userMobile = extractMobileFromJwt(authHeader);
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty() || !opt.get().getUserMobileNumber().equals(userMobile)) {
            throw new RuntimeException("Booking not found or not owned by user");
        }
        Booking booking = opt.get();
        if ("COMPLETED".equals(booking.getStatus()) || "WASHING".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot cancel completed or in-progress bookings");
        }
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Booking createBooking(BookingRequestDTO dto, String authHeader) {
        String userMobileNumber = extractMobileFromJwt(authHeader);

        // Fetch main service
        Optional<Service> serviceOpt = serviceRepository.findById(dto.getServiceId());
        if (!serviceOpt.isPresent()) throw new RuntimeException("Service not found");
        String serviceName = serviceOpt.get().getName();

        // Base service price
        double basePrice = priceService.getPrice(serviceName, dto.getCarType(), dto.isCeramicCoated());

        // Add-on prices
        double addOnTotal = 0.0;
        if (dto.getSelectedAddOnIds() != null && !dto.getSelectedAddOnIds().isEmpty()) {
            List<AddOn> selectedAddOns = addOnRepository.findAllById(dto.getSelectedAddOnIds());
            for (AddOn addOn : selectedAddOns) {
                addOnTotal += addOn.getPrice();
            }
        }

        // Final total price
        double finalPrice = basePrice + addOnTotal;

        // Create and save booking
        Booking booking = new Booking();
        booking.setUserMobileNumber(userMobileNumber);
        booking.setServiceId(dto.getServiceId());
        booking.setCarName(dto.getCarName());
        booking.setCarType(dto.getCarType());
        booking.setCeramicCoated(dto.isCeramicCoated());
        booking.setAddress(dto.getAddress());
        booking.setPickup(dto.isPickup());
        booking.setScheduledDateTime(dto.getScheduledDateTime());
        booking.setSelectedAddOnIds(dto.getSelectedAddOnIds());
        booking.setPrice(finalPrice);
        booking.setStatus("PENDING");
        return bookingRepository.save(booking);
    }

    public void rescheduleBooking(String bookingId, LocalDateTime newDateTime, String authHeader) {
        String userMobile = extractMobileFromJwt(authHeader);
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty() || !opt.get().getUserMobileNumber().equals(userMobile)) {
            throw new RuntimeException("Booking not found or not owned by user");
        }
        Booking booking = opt.get();
        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException("Can only reschedule pending bookings");
        }
        // Optional: Check if new slot is available (integrate with SlotService if needed)
        booking.setScheduledDateTime(newDateTime);
        bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForUser(String authHeader) {
        String userMobile = extractMobileFromJwt(authHeader);
        return bookingRepository.findByUserMobileNumber(userMobile);
    }

    private String extractMobileFromJwt(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
