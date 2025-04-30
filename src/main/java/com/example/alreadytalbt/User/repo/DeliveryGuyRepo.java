package com.example.alreadytalbt.User.repo;

import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryGuyRepo extends MongoRepository<DeliveryGuy, ObjectId> {
    // Your custom queries or methods can go here
}
