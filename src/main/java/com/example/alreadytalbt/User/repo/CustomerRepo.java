package com.example.alreadytalbt.User.repo;

import com.example.alreadytalbt.User.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
public interface CustomerRepo extends MongoRepository<Customer, ObjectId> {
    // You can add custom query methods here later if needed
}
