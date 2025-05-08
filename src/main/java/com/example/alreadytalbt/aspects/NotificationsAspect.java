package com.example.alreadytalbt.aspects;
import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotificationsAspect {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsAspect.class);

    @Autowired
    private OrderRepository orderRepository;

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

            logger.info(" Notifying customer {}: Your order {} status is now '{}'", customerId, orderId, newStatus);
        }
    }
}
