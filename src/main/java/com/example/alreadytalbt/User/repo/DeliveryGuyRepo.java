package com.example.alreadytalbt.User.repo;

import com.example.alreadytalbt.User.model.DeliveryGuy;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryGuyRepo extends MongoRepository<DeliveryGuy, ObjectId> {
    DeliveryGuy findByDeliveryId(ObjectId id);
}
