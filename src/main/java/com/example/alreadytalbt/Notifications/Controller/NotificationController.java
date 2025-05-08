package com.example.alreadytalbt.Notifications.Controller;

import com.example.alreadytalbt.Notifications.model.Notification;
import com.example.alreadytalbt.Notifications.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get all notifications for a specific customer
    @GetMapping("/{customerId}")
    public List<Notification> getNotificationsForCustomer(@PathVariable String customerId) {
        return notificationService.getNotificationsByCustomerId(customerId);
    }
}
