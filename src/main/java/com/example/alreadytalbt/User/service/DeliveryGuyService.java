package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.OrderRepository;
import com.example.alreadytalbt.User.dto.CreateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.UpdateDeliveryGuyDTO;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.repo.DeliveryGuyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryGuyService {

    @Autowired
    private DeliveryGuyRepo deliveryGuyRepo;

    public DeliveryGuy createDeliveryGuy(CreateDeliveryGuyDTO dto) {
        DeliveryGuy deliveryGuy = new DeliveryGuy(
                null,
                dto.getName(),
                dto.getAddress(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole(),
                dto.getOrderIds()
        );
        return deliveryGuyRepo.save(deliveryGuy);
    }

    @Autowired
    private OrderRepository orderRepository; // inject OrderRepository here

    public Order updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }



    public List<User> getAll() {
        return deliveryGuyRepo.findAll();
    }

    public Optional<UpdateDeliveryGuyDTO> updateDeliveryGuy(String id, UpdateDeliveryGuyDTO dto) {
        Optional<User> optionalDeliveryGuy = deliveryGuyRepo.findById(id);

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

}
