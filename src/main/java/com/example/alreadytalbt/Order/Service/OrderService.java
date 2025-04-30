package com.example.alreadytalbt.Order.Service;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import com.example.alreadytalbt.Order.dto.CreateOrderDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.repo.DeliveryGuyRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getById(String id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(CreateOrderDTO orderDTO) {
        Order order = new Order(
                orderDTO.getCustomerId(),
                orderDTO.getRestaurantId(),
                orderDTO.getDeliveryGuyId(),
                orderDTO.getItems(),
                orderDTO.getStatus(),
                orderDTO.getPaymentMethod()
        );
        return orderRepository.save(order);
    }

    public Order updateOrder(String id, UpdateOrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));

        BeanUtils.copyProperties(orderDTO, existingOrder, getNullPropertyNames(orderDTO));

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Autowired
    private DeliveryGuyRepo deliveryGuyRepo;

    public Order assignDeliveryGuy(String orderId, String deliveryGuyId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryGuy deliveryGuy = (DeliveryGuy) deliveryGuyRepo.findById(deliveryGuyId)
                .orElseThrow(() -> new RuntimeException("Delivery guy not found"));

        // Assign delivery guy to the order
        order.setDeliveryGuyId(deliveryGuyId);
        Order savedOrder = orderRepository.save(order);

        // Append orderId to delivery guy's orderIds list
        deliveryGuy.addOrderId(orderId);
        deliveryGuyRepo.save(deliveryGuy);

        return savedOrder;
    }


    public List<Order> getOrdersByDeliveryGuy(String deliveryGuyId) {
        return orderRepository.findByDeliveryGuyId(deliveryGuyId);
    }
}
