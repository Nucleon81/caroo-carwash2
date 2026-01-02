package com.practice.project_1.repository;

import com.practice.project_1.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserMobileNumber(String mobileNumber);
    List<Booking> findByScheduledDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
