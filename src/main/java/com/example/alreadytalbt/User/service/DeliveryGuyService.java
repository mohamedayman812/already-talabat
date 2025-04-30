package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.dto.CreateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.UpdateDeliveryGuyDTO;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.repo.DeliveryGuyRepo;
import com.example.alreadytalbt.User.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        deliveryGuy.setName(user.getName());
        deliveryGuy.setEmail(user.getEmail());
        deliveryGuy.setRole(user.getRole());
        deliveryGuy.setAddress(user.getAddress());
        deliveryGuy.setPassword(user.getPassword());
        deliveryGuy.setOrderIds(dto.getOrderIds());
        deliveryGuy.setUserId(user.getId()); // reference to user

        // Save to deliveryGuys collection
        deliveryGuyRepo.save(deliveryGuy);

        return deliveryGuy;
    }






    public Order updateOrderStatus(ObjectId orderId, String newStatus) {
        return orderFeignClient.updateOrderStatus(orderId, newStatus);
    }



    public List<DeliveryGuy> getAll() {
        return deliveryGuyRepo.findAll();
    }

    public Optional<UpdateDeliveryGuyDTO> updateDeliveryGuy(ObjectId id, UpdateDeliveryGuyDTO dto) {
        Optional<DeliveryGuy> optionalDeliveryGuy = deliveryGuyRepo.findById(id);

        if (optionalDeliveryGuy.isPresent()) {
            DeliveryGuy deliveryGuy = (DeliveryGuy) optionalDeliveryGuy.get();

            // Update only non-null values from the DTO
            if (dto.getName() != null) {
                deliveryGuy.setName(dto.getName());
            }
            if (dto.getEmail() != null) {
                deliveryGuy.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null) {
                deliveryGuy.setPassword(dto.getPassword());
            }
            if (dto.getAddress() != null) {
                deliveryGuy.setAddress(dto.getAddress());
            }
            if (dto.getOrderIds() != null) {
                deliveryGuy.setOrderIds(dto.getOrderIds());
            }


            // Save the updated DeliveryGuy entity
            DeliveryGuy updated = deliveryGuyRepo.save(deliveryGuy);

            // Return a response DTO with the updated fields
            return Optional.of(new UpdateDeliveryGuyDTO(
                    updated.getName(),
                    updated.getPassword(),
                    updated.getEmail(),
                    updated.getAddress(),
                    updated.getOrderIds()

            ));
        }

        return Optional.empty();
    }



    public boolean deleteDeliveryGuy(ObjectId id) {
        if (deliveryGuyRepo.existsById(id)) {
            deliveryGuyRepo.deleteById(id);
            return true;
        }
        return false;
    }

//    private CreateDeliveryGuyDTO mapToDTO(DeliveryGuy deliveryGuy) {
//        CreateDeliveryGuyDTO dto = new CreateDeliveryGuyDTO();
//        dto.setId(deliveryGuy.getId());
//        dto.setName(deliveryGuy.getName());
//        dto.setEmail(deliveryGuy.getEmail());
//        dto.setPassword(deliveryGuy.getPassword());
//        dto.setAddress(deliveryGuy.getAddress());
//        dto.setOrderIds(deliveryGuy.getOrderIds());
//        return dto;
//    }






    public void assignOrderToDeliveryGuy( ObjectId deliveryGuyId,ObjectId orderId) {
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(deliveryGuyId)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));

        if (!deliveryGuy.getOrderIds().contains(orderId)) {
            deliveryGuy.getOrderIds().add(orderId);
            deliveryGuyRepo.save(deliveryGuy);
        }
    }


}
