package com.practice.project_1.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    private String id;

    private String userMobileNumber;
    private String serviceId;
    private String serviceName;
    private String carName;
    private CarType carType;
    private boolean ceramicCoated;
    private Location address;
    private boolean isPickup;
    private double price;
    private String status;
    private LocalDateTime scheduledDateTime;
   private List<String> selectedAddOnIds; // Add-on IDs
    private String review;  // User review text
    private Integer rating; // 1-5 stars
}