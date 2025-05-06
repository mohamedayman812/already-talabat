package com.example.alreadytalbt.User.repo;



import com.example.alreadytalbt.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, ObjectId> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
