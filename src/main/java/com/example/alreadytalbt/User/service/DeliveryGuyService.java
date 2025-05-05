package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.dto.CreateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.UpdateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.VendorResponseDTO;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.model.Vendor;
import com.example.alreadytalbt.User.repo.DeliveryGuyRepo;
import com.example.alreadytalbt.User.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryGuyService {

    @Autowired
    private DeliveryGuyRepo deliveryGuyRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderFeignClient orderFeignClient;



    public DeliveryGuy createDeliveryGuy(CreateDeliveryGuyDTO dto) {

        // First, create a User object
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.DELIVERY);
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword());

        // Save the User and get the generated ID
        user = userRepo.save(user);

        // Then, create the DeliveryGuy and link it to the User ID
        DeliveryGuy deliveryGuy = new DeliveryGuy();

        deliveryGuy.setOrderIds(dto.getOrderIds());
        deliveryGuy.setUserId(user.getId()); // reference to user

        // Save to deliveryGuys collection
        deliveryGuyRepo.save(deliveryGuy);

        return deliveryGuy;
    }






    public OrderResponseDTO updateOrderStatus(String orderId, String newStatus) {
        System.out.println("new staus: "+newStatus);
        return orderFeignClient.updateOrderStatus(orderId, newStatus);
    }




    public UpdateDeliveryGuyDTO getDeliveryGuyById(ObjectId id) {
        return mapToDTO(deliveryGuyRepo.findByDeliveryId(id));
    }



    public UpdateDeliveryGuyDTO updateDeliveryGuy(ObjectId id, UpdateDeliveryGuyDTO dto) {

        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));

        User user = userRepo.findById(deliveryGuy.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for DeliveryGuy"));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
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





    public boolean deleteDeliveryGuy(ObjectId id) {
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));
        if (deliveryGuyRepo.existsById(id)) {

            userRepo.deleteById(deliveryGuy.getUserId());
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
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setPassword(user.getPassword());
        dto.setOrderIds(deliveryGuy.getOrderIds());

        return dto;
    }






    public void assignOrderToDeliveryGuy( ObjectId orderId,ObjectId deliveryGuyId) {
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(deliveryGuyId)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));

        if (!deliveryGuy.getOrderIds().contains(orderId)) {
            deliveryGuy.getOrderIds().add(orderId);
            deliveryGuyRepo.save(deliveryGuy);
        }
        orderFeignClient.assignOrderToDeliveryGuy(orderId,deliveryGuyId);


    }

    public List<OrderSummaryDTO> getAllOrdersForDeliveryGuy() {
        return orderFeignClient.getOrderSummaries();
    }

}
