package com.example.alreadytalbt.User.repo;



import com.example.alreadytalbt.User.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {
    List<User> findByRole(String role);
}

