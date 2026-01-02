package com.practice.project_1.dto;

import com.practice.project_1.model.CarType;
import com.practice.project_1.model.Location;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingRequestDTO {
    private String serviceId;
    private String carName;
    private CarType carType;
    private boolean ceramicCoated;
    private Location address;
    private boolean isPickup;
    private LocalDateTime scheduledDateTime;
    private List<String> selectedAddOnIds; // Optional add-ons
}
