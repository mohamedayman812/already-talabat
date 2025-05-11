package com.example.alreadytalbt.User.repo;

import com.example.alreadytalbt.User.model.DeliveryGuy;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeliveryGuyRepo extends MongoRepository<DeliveryGuy, ObjectId> {
    DeliveryGuy findByDeliveryId(ObjectId id);
    Optional<DeliveryGuy> findByUserId(ObjectId userId);
}
