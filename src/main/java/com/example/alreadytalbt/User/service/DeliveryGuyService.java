package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.dto.CreateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
import com.example.alreadytalbt.User.dto.DeliveryGuyResponseDTO;
import com.example.alreadytalbt.User.dto.UpdateDeliveryGuyDTO;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.repo.DeliveryGuyRepo;
import com.example.alreadytalbt.User.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.alreadytalbt.User.auth.JwtUtil;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeliveryGuyService {

    @Autowired
    private DeliveryGuyRepo deliveryGuyRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private JwtUtil jwtUtil;


    public DeliveryGuyResponseDTO createDeliveryGuy(CreateDeliveryGuyDTO dto, String userId) {


        User user = userRepo.findById(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.DELIVERY) {
            throw new RuntimeException("User is not a DeliveryGuy");
        }

        DeliveryGuy delivery = new DeliveryGuy();
        delivery.setUserId(new ObjectId(userId));
        delivery.setOrderIds(dto.getOrderIds());


        deliveryGuyRepo.save(delivery);
        return mapToResponse(delivery);
    }

    public OrderResponseDTO updateOrderStatus(String orderId, String newStatus) {
        System.out.println("new staus: "+newStatus);
        return orderFeignClient.updateOrderStatus(orderId, newStatus);
    }


    public UpdateDeliveryGuyDTO getDeliveryGuyById(String UserId) {
        ObjectId id = getDeliveryGuyIdFromUserId(UserId);
        return mapToDTO(deliveryGuyRepo.findByDeliveryId(id));
    }


    public UpdateDeliveryGuyDTO updateDeliveryGuy( UpdateDeliveryGuyDTO dto, String UserId) {
        ObjectId id = getDeliveryGuyIdFromUserId(UserId);
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));

        User user = userRepo.findById(new ObjectId(UserId))
                .orElseThrow(() -> new RuntimeException("User not found for DeliveryGuy"));

        if (dto.getName() != null) user.setUsername(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        userRepo.save(user);


            if (dto.getOrderIds() != null) {
                deliveryGuy.setOrderIds(dto.getOrderIds());
            }


            // Save the updated DeliveryGuy entity
          deliveryGuyRepo.save(deliveryGuy);

            // Return a response DTO with the updated fields
            return mapToDTO(deliveryGuy);
        }


    public boolean deleteDeliveryGuy(String UserId) {
        ObjectId id = getDeliveryGuyIdFromUserId(UserId);
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));
        if (deliveryGuyRepo.existsById(id)) {

            userRepo.deleteById(new ObjectId(UserId));
            deliveryGuyRepo.deleteById(id);

            return true;
        }
        return false;
    }

    private UpdateDeliveryGuyDTO mapToDTO(DeliveryGuy deliveryGuy) {
        UpdateDeliveryGuyDTO dto = new UpdateDeliveryGuyDTO();
        User user = userRepo.findById(deliveryGuy.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for DeliveryGuy"));
        dto.setDeliveryid(deliveryGuy.getDeliveryid().toHexString());
        dto.setUserId(deliveryGuy.getUserId().toHexString());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setOrderIds(deliveryGuy.getOrderIds());
        dto.setPhone(user.getPhone());

        return dto;
    }

    private DeliveryGuyResponseDTO mapToResponse(DeliveryGuy deliveryGuy) {
        User user = userRepo.findById(deliveryGuy.getUserId())
                .orElseThrow(() -> new RuntimeException(("User not found for customer")));

        DeliveryGuyResponseDTO dto = new DeliveryGuyResponseDTO();
        dto.setId(deliveryGuy.getDeliveryid().toHexString());
        dto.setUserId(user.getId().toHexString());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setOrderIds(
                deliveryGuy.getOrderIds() != null
                        ? deliveryGuy.getOrderIds().stream()
                        .map(ObjectId::toHexString)
                        .toList()
                        : List.of()
        );


        return dto;
    }

    public ObjectId getDeliveryGuyIdFromUserId(String userIdStr) {
        ObjectId userId;
        try {
            userId = new ObjectId(userIdStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid userId format: " + userIdStr);
        }

        return deliveryGuyRepo.findByUserId(userId)
                .map(DeliveryGuy::getDeliveryid)
                .orElseThrow(() -> new RuntimeException("Delivery guy not found for userId: " + userIdStr));
    }

    public void assignOrderToDeliveryGuy( ObjectId orderId ,String UserId) {
        System.out.println("fel service");
        System.out.println(UserId);
        ObjectId deliveryGuyId = getDeliveryGuyIdFromUserId(UserId);
        System.out.println(deliveryGuyId);
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(deliveryGuyId)
                .orElseThrow(() -> new RuntimeException("Delivery guy not found"));

        if (!deliveryGuy.getUserId().toHexString().equals(UserId)) {
            throw new RuntimeException("You are not authorized to update this customer");
        }


        if (!deliveryGuy.getOrderIds().contains(orderId)) {
            deliveryGuy.getOrderIds().add(orderId);
            deliveryGuyRepo.save(deliveryGuy);
        }
        orderFeignClient.assignOrderToDeliveryGuy(orderId, deliveryGuyId);



    }

    public List<OrderResponseDTO> getAllOrdersForDeliveryGuy() {
        return orderFeignClient.getOrderSummaries();
    }

}
