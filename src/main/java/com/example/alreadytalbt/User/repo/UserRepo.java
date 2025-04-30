package com.example.alreadytalbt.User.repo;



import com.example.alreadytalbt.User.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
    // You can define custom queries here if needed
}

