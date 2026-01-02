package com.practice.project_1.repository;

import com.practice.project_1.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<Service, String> {
}
