package com.practice.project_1.controller;

import com.practice.project_1.model.Service;
import com.practice.project_1.repository.ServiceRepository;
import com.practice.project_1.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired private ServiceRepository serviceRepository;

    // Public GET endpoint (for everyone, no auth required)
    @GetMapping
    public List<Service> getAllServices() {
        return serviceService.getAllServices();
    }

    // Admin only: add @PreAuthorize if you use admin roles and Spring Security method security
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service created = serviceService.createService(service);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable String id) {
        Service s = serviceService.getServiceById(id);
        if (s != null)
            return ResponseEntity.ok(s);
        else
            return ResponseEntity.notFound().build();
    }
    // In ServiceController.java
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable String id) {
        serviceRepository.deleteById(id);
        return ResponseEntity.ok("Service deleted");
    }

}
