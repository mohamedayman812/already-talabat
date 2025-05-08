package com.example.alreadytalbt.User.repo;



import com.example.alreadytalbt.User.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, ObjectId> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
