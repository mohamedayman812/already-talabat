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
        // Create a new DeliveryGuy and set properties using setters
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setName(dto.getName());
        deliveryGuy.setEmail(dto.getEmail());
        deliveryGuy.setRole(Role.DELIVERY);
        deliveryGuy.setAddress(dto.getAddress());
        deliveryGuy.setPassword(dto.getPassword());
        deliveryGuy.setOrderIds(dto.getOrderIds()); // Ensure DTO has orderIds field

        // Save the DeliveryGuy
        deliveryGuyRepo.save(deliveryGuy);
        userRepo.save(deliveryGuy);

        // Return the mapped DTO
        return deliveryGuy;
    }




    public Order updateOrderStatus(String orderId, String newStatus) {
        return orderFeignClient.updateOrderStatus(orderId, newStatus);
    }



    public List<DeliveryGuy> getAll() {
        return deliveryGuyRepo.findAll();
    }

    public Optional<UpdateDeliveryGuyDTO> updateDeliveryGuy(String id, UpdateDeliveryGuyDTO dto) {
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
                    updated.getId(),
                    updated.getName(),
                    updated.getEmail(),
                    updated.getAddress(),
                    updated.getOrderIds()

            ));
        }

        return Optional.empty();
    }



    public boolean deleteDeliveryGuy(String id) {
        if (deliveryGuyRepo.existsById(id)) {
            deliveryGuyRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private CreateDeliveryGuyDTO mapToDTO(DeliveryGuy deliveryGuy) {
        CreateDeliveryGuyDTO dto = new CreateDeliveryGuyDTO();
        dto.setId(deliveryGuy.getId());
        dto.setName(deliveryGuy.getName());
        dto.setEmail(deliveryGuy.getEmail());
        dto.setPassword(deliveryGuy.getPassword());
        dto.setAddress(deliveryGuy.getAddress());
        dto.setOrderIds(deliveryGuy.getOrderIds());
        return dto;
    }






    public void assignOrderToDeliveryGuy( String deliveryGuyId,String orderId) {
        DeliveryGuy deliveryGuy = deliveryGuyRepo.findById(deliveryGuyId)
                .orElseThrow(() -> new NoSuchElementException("Delivery guy not found"));

        if (!deliveryGuy.getOrderIds().contains(orderId)) {
            deliveryGuy.getOrderIds().add(orderId);
            deliveryGuyRepo.save(deliveryGuy);
        }
    }


}
