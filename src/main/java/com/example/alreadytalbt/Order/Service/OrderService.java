package com.example.alreadytalbt.Order.Service;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import com.example.alreadytalbt.Order.dto.CreateOrderDTO;
import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
//import com.example.alreadytalbt.Order.feign.DeliveryGuyFeignClient;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
//    @Autowired
//    private DeliveryGuyFeignClient deliveryFeign;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderResponseDTO getById(ObjectId id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDto(order);
    }


    public Order createOrder(CreateOrderDTO orderDTO) {
        Order order = new Order(
                orderDTO.getCustomerId(),
                orderDTO.getRestaurantId(),
                orderDTO.getDeliveryGuyId(),
                orderDTO.getCartId(),
                orderDTO.getItems(),
                orderDTO.getStatus(),
                orderDTO.getPaymentMethod()
        );
        return orderRepository.save(order);
    }

    public Order updateOrder(ObjectId id, UpdateOrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));

        BeanUtils.copyProperties(orderDTO, existingOrder, getNullPropertyNames(orderDTO));

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(ObjectId id) {
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



//    public Optional<Order> assignOrderToDelivery(
//            @PathVariable("orderId") String orderId,
//            @PathVariable("deliveryGuyId") String deliveryGuyId)
//           {
//               Optional<Order> optionalOrder = orderRepository.findById(orderId);
//
//               Order order = optionalOrder.get();
//               order.setDeliveryGuyId(deliveryGuyId);
//
//        // 2. Assign the order to the delivery guy by calling the Feign client for the delivery guy service
//        feign.assignOrderToDelivery(orderId,deliveryGuyId);
//
//
//        return getById(orderId); // Assuming you have a method to fetch an order
//    }

    public Order assignOrderToDelivery(ObjectId orderId, ObjectId deliveryGuyId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        System.out.println("YARAB");
        order.setDeliveryGuyId(deliveryGuyId);
        System.out.println(order.toString());
        orderRepository.save(order);

        // Call DeliveryGuyService to update delivery guy record
       // deliveryFeign.assignOrderToDeliveryGuy(orderId, deliveryGuyId);

        return order;
    }



    public List<Order> getOrdersByDeliveryGuy(ObjectId deliveryGuyId) {
        return orderRepository.findByDeliveryGuyId(deliveryGuyId);
    }

    public OrderResponseDTO updateOrderStatus(String orderId, String status) {

        Optional<Order> optionalOrder = orderRepository.findById(new ObjectId(orderId));
        if (optionalOrder.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " not found");
        }

        Order order = optionalOrder.get();
        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    public List<OrderResponseDTO> getAllOrderSummaries() {
        System.out.println(orderRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()).toString());
       return orderRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByCustomerId(ObjectId customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<OrderResponseDTO> getOrdersByRestaurantId(ObjectId restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    public static UpdateOrderDTO toDTO(Order order) {
        UpdateOrderDTO dto = new UpdateOrderDTO();
        //dto.setId(order.getId().toString());
        dto.setStatus(order.getStatus());
        return dto;
    }


    private OrderResponseDTO convertToDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setId(order.getId().toHexString());
        dto.setRestaurantId(order.getRestaurantId().toHexString());
        dto.setCustomerId(order.getCustomerId().toHexString());
        dto.setDeliveryGuyId(order.getDeliveryGuyId()==null? "":order.getDeliveryGuyId().toHexString());
        dto.setStatus(order.getStatus());
        List<String> itemIds = order.getItems()
                .stream()
                .map(ObjectId::toHexString)
                .collect(Collectors.toList());
        dto.setItems(itemIds);
        dto.setPaymentMethod(order.getPaymentMethod());
        return dto;
    }




}
