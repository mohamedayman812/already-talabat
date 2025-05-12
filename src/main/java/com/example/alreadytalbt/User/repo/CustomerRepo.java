package com.example.alreadytalbt.User.repo;

import com.example.alreadytalbt.User.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface CustomerRepo extends MongoRepository<Customer, ObjectId> {
    Optional<Customer> findByUserId(ObjectId userId);
}
