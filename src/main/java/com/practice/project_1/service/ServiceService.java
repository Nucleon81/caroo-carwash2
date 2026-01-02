package com.practice.project_1.service;

import com.practice.project_1.model.Service;
import com.practice.project_1.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service getServiceById(String id) {
        Optional<Service> opt = serviceRepository.findById(id);
        return opt.orElse(null);
    }
}
