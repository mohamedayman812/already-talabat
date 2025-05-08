package com.example.alreadytalbt.Notifications.Service;

import com.example.alreadytalbt.Notifications.model.Notification;
import com.example.alreadytalbt.Notifications.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyCustomer(String customerId, String message) {
        Notification notification = new Notification(customerId, message);
        notificationRepository.save(notification);
    }
}
