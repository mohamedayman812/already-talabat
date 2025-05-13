package com.example.alreadytalbt.User.Notifications.Controller;

import com.example.alreadytalbt.User.Notifications.model.Notification;
import com.example.alreadytalbt.User.Notifications.Service.NotificationService;
import com.example.alreadytalbt.User.dto.AuthUserResponseDTO;
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
    public List<Notification> getNotificationsForCustomer(@PathVariable String customerId, AuthUserResponseDTO dto) {
        return notificationService.getNotificationsByCustomerId(customerId,dto);
    }
}
