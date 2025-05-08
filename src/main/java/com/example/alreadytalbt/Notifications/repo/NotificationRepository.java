package com.example.alreadytalbt.Notifications.repo;

import com.example.alreadytalbt.Notifications.model.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, ObjectId> {
    List<Notification> findByCustomerId(String customerId);
}
