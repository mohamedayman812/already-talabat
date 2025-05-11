package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import com.example.alreadytalbt.Restaurant.dto.*;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.dto.*;

import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.model.Vendor;
import com.example.alreadytalbt.User.repo.UserRepo;
import com.example.alreadytalbt.User.repo.VendorRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    private VendorRepo vendorRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestaurantClient restaurantClient;

    @Autowired
    private final OrderFeignClient orderFeignClient;

    @Autowired
    private JwtUtil jwtUtil;

    public VendorService(OrderFeignClient orderFeignClient, RestaurantClient restaurantClient) {
        this.orderFeignClient = orderFeignClient;
        this.restaurantClient = restaurantClient;
    }


    public VendorResponseDTO createVendor(VendorCreateDTO dto,String token) {
        String userId = jwtUtil.extractUserId(token);

        // Optional: check that user role is CUSTOMER
        User user = userRepo.findById(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.VENDOR) {
            throw new RuntimeException("User is not a vendor");
        }

        //Create and save the Vendor
        Vendor vendor = new Vendor();
        vendor.setUserId(user.getId());
        Vendor savedVendor = vendorRepository.save(vendor);

        //Create Restaurant
        CreateRestaurantDTO restaurantDTO = new CreateRestaurantDTO();
        restaurantDTO.setRestaurantName(dto.getRestaurantName());
        restaurantDTO.setRestaurantAddress(dto.getRestaurantAddress());
        restaurantDTO.setVendorId(savedVendor.getId().toHexString());

        RestaurantResponseDTO createdRestaurant = restaurantClient.createRestaurant(restaurantDTO);
        ObjectId restID = new ObjectId(createdRestaurant.getId());
        savedVendor.setRestaurantId(restID);
        vendorRepository.save(savedVendor);

        return mapToResponse(savedVendor);
    }

    public RestaurantResponseDTO getVendorRestaurant(String restaurantId) {
        return restaurantClient.getRestaurantById(restaurantId);
    }

    public VendorResponseDTO getVendorById(ObjectId id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return mapToResponse(vendor);
    }

    public List<VendorResponseDTO> getAllVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VendorResponseDTO getVendorWithUser(ObjectId vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));


        User user = userRepo.findById(vendor.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for vendor"));


        VendorResponseDTO responseDTO = new VendorResponseDTO();
        responseDTO.setVendorId(vendor.getId().toHexString());
        responseDTO.setUserId(user.getId().toHexString());
        responseDTO.setName(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRestaurantId(vendor.getRestaurantId().toHexString());


        if (vendor.getRestaurantId() != null) {
            try {
                RestaurantResponseDTO restaurant = restaurantClient.getRestaurantById(vendor.getRestaurantId().toHexString());
                responseDTO.setRestaurantName(restaurant.getName());
                responseDTO.setRestaurantAddress(restaurant.getAddress());
            } catch (Exception e) {
                System.out.println("Restaurant fetch failed: " + e.getMessage());
            }
        }

        return responseDTO;
    }

    public VendorResponseDTO updateVendor(ObjectId id, VendorUpdateDto dto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));


        User user = userRepo.findById(vendor.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for vendor"));

        if (dto.getName() != null) user.setUsername(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        userRepo.save(user);


        if (vendor.getRestaurantId() != null &&
                (dto.getRestaurantName() != null || dto.getRestaurantAddress() != null)) {

            RestaurantUpdateDto restaurantUpdateDTO = new RestaurantUpdateDto();

            restaurantUpdateDTO.setName(dto.getRestaurantName());
            restaurantUpdateDTO.setAddress(dto.getRestaurantAddress());

            try {
                restaurantClient.updateRestaurants(vendor.getRestaurantId().toHexString(), restaurantUpdateDTO);
            } catch (Exception e) {
                System.out.println("Failed to update restaurant: " + e.getMessage());
            }
        }

        return mapToResponse(vendor);
    }

    public void deleteVendor(ObjectId id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));


        if (vendor.getRestaurantId() != null) {
            try {
                restaurantClient.deleteRestaurants(vendor.getRestaurantId().toHexString());
            } catch (Exception e) {
                System.out.println("Failed to delete restaurant: " + e.getMessage());
            }
        }

        try {
            userRepo.deleteById(vendor.getUserId());
        } catch (Exception e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }


        vendorRepository.deleteById(id);
    }

    public MenuItemDTO createMenuItem(ObjectId vendorId, MenuItemCreateDTO dto) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (vendor.getRestaurantId() == null)
            throw new RuntimeException("Vendor does not have a restaurant");

        dto.setRestaurantId(vendor.getRestaurantId().toHexString());
        return restaurantClient.createMenuItem(dto);
    }

    public MenuItemDTO updateMenuItem(String menuItemId, MenuItemUpdateDTO dto, String vendorId) {
        MenuItemDTO menuItem = restaurantClient.getMenuItemById(menuItemId);


        RestaurantResponseDTO restaurant = restaurantClient.getRestaurantById(menuItem.getRestauarantId());

        if (!restaurant.getVendorId().equals(vendorId)) {
            throw new RuntimeException("Unauthorized: Vendor does not own this menu item.");
        }

        return restaurantClient.updateMenuItems(menuItemId, dto);
    }

    public void deleteMenuItem(String menuItemId, String vendorId) {
        MenuItemDTO menuItem = restaurantClient.getMenuItemById(menuItemId);

        RestaurantResponseDTO restaurant = restaurantClient.getRestaurantById(menuItem.getRestauarantId());

        if (!restaurant.getVendorId().equals(vendorId)) {
            throw new RuntimeException("Unauthorized: Vendor does not own this menu item.");
        }
        restaurantClient.deleteMenuItems(menuItemId);
    }

    public List<OrderResponseDTO> getOrder(String vendorId) {
        Vendor vendor = vendorRepository.findById(new ObjectId(vendorId))
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        String restaurantId = vendor.getRestaurantId().toHexString();

        // Step 2: Get all orders for this restaurant from the order service
        return orderFeignClient.getOrdersByRestaurantId(restaurantId);
    }

    public OrderResponseDTO updateOrderStatus(String orderId, String  vendorId, String token){
        System.out.println("vendor id: "+ vendorId);
        Vendor vendor = vendorRepository.findById(new ObjectId(vendorId))
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        String newStatus ="Prepared";

        OrderResponseDTO order = orderFeignClient.getOrderById(orderId);

        if (!vendor.getRestaurantId().toHexString().equals(order.getRestaurantId())) {
            throw new RuntimeException("You are not authorized to update this order's status.");
        }

        return orderFeignClient.updateOrderStatus(orderId, newStatus);
    }




    private VendorResponseDTO mapToResponse(Vendor vendor) {
        VendorResponseDTO dto = new VendorResponseDTO();
        User user = userRepo.findById(vendor.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for vendor"));
        dto.setVendorId(vendor.getId().toHexString());
        dto.setUserId(vendor.getUserId().toHexString());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRestaurantId(vendor.getRestaurantId().toHexString());
        if (vendor.getRestaurantId() != null) {
            try {
                RestaurantResponseDTO restaurant = restaurantClient.getRestaurantById(vendor.getRestaurantId().toHexString());
                dto.setRestaurantName(restaurant.getName());
                dto.setRestaurantAddress(restaurant.getAddress());
            } catch (Exception e) {
                System.out.println("Restaurant fetch failed: " + e.getMessage());
            }
        }

        return dto;
    }




}
