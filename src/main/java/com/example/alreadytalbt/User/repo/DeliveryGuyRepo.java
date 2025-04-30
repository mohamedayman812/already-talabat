package com.example.alreadytalbt.User.repo;

import com.example.alreadytalbt.User.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryGuyRepo extends MongoRepository<User, String> {

}