package com.example.alreadytalbt.User.Notifications.Service;

import com.example.alreadytalbt.User.Notifications.model.Notification;
import com.example.alreadytalbt.User.Notifications.repo.NotificationRepository;
import com.example.alreadytalbt.User.dto.AuthUserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    public void notifyCustomer(String customerId, String message) {
        Notification notification = new Notification(customerId, message);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByCustomerId(String customerId, AuthUserResponseDTO dto) {
        System.out.println("Getting notifications for customer ID: " + customerId);

        List<Notification> notifications = notificationRepository.findByCustomerId(customerId);

        List<String> notificationIds = new ArrayList<>();
        for (Notification notification : notifications) {
            System.out.println(notification.getId() + ": " + notification.getMessage() + "\n");
            notificationIds.add(notification.getMessage());
        }

        dto.setNotificationIds(notificationIds);

        return notifications;
    }




}
