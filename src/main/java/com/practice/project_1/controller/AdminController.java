package com.practice.project_1.controller;

import com.practice.project_1.dto.PushNotificationRequest;
import com.practice.project_1.model.*;
import com.practice.project_1.repository.AddOnRepository;
import com.practice.project_1.repository.BookingRepository;
import com.practice.project_1.repository.ServiceRepository;
import com.practice.project_1.repository.UserRepository;
import com.practice.project_1.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private AddOnRepository addOnRepository;
    @Autowired private PushNotificationService pushNotificationService;

    // Users Management
    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @PatchMapping("/users/{mobileNumber}")
    public Users updateUser(@PathVariable String mobileNumber, @RequestBody Users updates) {
        Users user = userRepository.findById(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (updates.getRole() != null) user.setRole(updates.getRole());
        if (updates.getUserEmail() != null) user.setUserEmail(updates.getUserEmail());
        // Add more updatable fields as needed
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{mobileNumber}")
    public ResponseEntity<String> deleteUser(@PathVariable String mobileNumber) {
        if (!userRepository.existsById(mobileNumber)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(mobileNumber);
        return ResponseEntity.ok("User deleted");
    }

    // Services Management
    @GetMapping("/services")
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @PostMapping("/services")
    public Service addService(@RequestBody Service service) {
        return serviceRepository.save(service);
    }

    @PatchMapping("/services/{id}")
    public Service updateService(@PathVariable String id, @RequestBody Service updates) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (updates.getName() != null) {
            service.setName(updates.getName());
        }
        if (updates.getPrice() != null && updates.getPrice() > 0) {
            service.setPrice(updates.getPrice());
        }
        if (updates.getDescription() != null) {
            service.setDescription(updates.getDescription());
        }

        return serviceRepository.save(service);
    }

//    @PatchMapping("/services/{id}")
//    public Service updateService(@PathVariable String id, @RequestBody Service updates) {
//        Service service = serviceRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//        if (updates.getName() != null) service.setName(updates.getName());
//        if (updates.getPrice() > 0) service.setPrice(updates.getPrice());
//        return serviceRepository.save(service);
//    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<String> deleteService(@PathVariable String id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        serviceRepository.deleteById(id);
        return ResponseEntity.ok("Service deleted");
    }

    // Add-Ons Management
    @GetMapping("/addons")
    public List<AddOn> getAllAddOns() {
        return addOnRepository.findAll();
    }

    @PostMapping("/addons")
    public AddOn addAddOn(@RequestBody AddOn addOn) {
        return addOnRepository.save(addOn);
    }

    @PatchMapping("/addons/{id}")
    public AddOn updateAddOn(@PathVariable String id, @RequestBody AddOn updates) {
        AddOn addOn = addOnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Add-On not found"));
        if (updates.getName() != null) addOn.setName(updates.getName());
        if (updates.getPrice() > 0) addOn.setPrice(updates.getPrice());
        return addOnRepository.save(addOn);
    }

    @DeleteMapping("/addons/{id}")
    public ResponseEntity<String> deleteAddOn(@PathVariable String id) {
        if (!addOnRepository.existsById(id)) {
            throw new RuntimeException("Add-On not found");
        }
        addOnRepository.deleteById(id);
        return ResponseEntity.ok("Add-On deleted");
    }
    // Bookings Management
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        bookings.forEach(booking -> {
            Optional<Service> service = serviceRepository.findById(booking.getServiceId());
            booking.setServiceName(service.isPresent() ? service.get().getName() : "Unknown");
        });
        return bookings;
    }

    @PostMapping("/notifications/push")
    public ResponseEntity<Map<String, Object>> pushNotification(
            @RequestBody PushNotificationRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        int sent = pushNotificationService.sendBroadcast(request);

        Map<String, Object> resp = new HashMap<>();
        resp.put("sent", sent);
        return ResponseEntity.ok(resp);
    }


    @PatchMapping("/bookings/{id}/status")
    public Booking updateBookingStatus(@PathVariable String id, @RequestParam String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @PatchMapping("/bookings/{id}")
    public Booking updateBooking(@PathVariable String id, @RequestBody Booking updates) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (updates.getPrice() > 0) booking.setPrice(updates.getPrice());
        // Add other updatable fields (e.g., scheduledDateTime) as needed
        return bookingRepository.save(booking);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable String id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.ok("Booking deleted");
    }


    // Analytics
    @GetMapping("/analytics")
    public Map<String, Object> getAnalytics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("totalServices", serviceRepository.count());
        stats.put("totalAddOns", addOnRepository.count());
        // Add more stats (e.g., total revenue: use aggregation if needed)
        return stats;
    }
    // Get single booking by ID
    @GetMapping("/bookings/{id}")
    public Booking getBookingById(@PathVariable String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

}
