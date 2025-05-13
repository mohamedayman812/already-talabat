
package com.example.alreadytalbt.aspects;

import com.example.alreadytalbt.User.Notifications.Service.NotificationService;
import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class NotificationsAspect {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationService notificationService;

    @AfterReturning(
            pointcut = "execution(* com.example.alreadytalbt.Order.Service.OrderService.updateOrderStatus(..))",
            returning = "result"
    )
    public void notifyUserAfterStatusUpdate(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        String orderId = (String) args[0];
        String newStatus = (String) args[1];

        Order order = orderRepository.findById(new ObjectId(orderId)).orElse(null);
        if (order != null) {
            String customerId = order.getCustomerId().toHexString();
            notificationService.notifyCustomer(customerId, "Your order status has been updated to: " + newStatus);
        }
    }
}
