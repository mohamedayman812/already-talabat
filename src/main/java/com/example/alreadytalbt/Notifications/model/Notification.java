package com.example.alreadytalbt.Notifications.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
public class Notification {

    @Id
    private ObjectId id;
    private String customerId;
    private String message;
    private LocalDateTime timestamp;

    public Notification(String customerId, String message) {
        this.customerId = customerId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
}
