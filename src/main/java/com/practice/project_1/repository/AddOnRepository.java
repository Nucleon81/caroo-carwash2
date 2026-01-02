package com.practice.project_1.repository;

import com.practice.project_1.model.AddOn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddOnRepository extends MongoRepository<AddOn, String> {
}
