package com.practice.project_1.repository;

import com.practice.project_1.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findByMobileNumber(String mobileNumber);
    List<Users> findByFcmTokenNotNull();

}
