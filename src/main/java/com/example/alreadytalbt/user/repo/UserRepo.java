package com.example.alreadytalbt.user.repo;



import com.example.alreadytalbt.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
    // You can define custom queries here if needed
}

