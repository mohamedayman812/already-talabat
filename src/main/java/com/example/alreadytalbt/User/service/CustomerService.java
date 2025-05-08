package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
import com.example.alreadytalbt.User.dto.UpdateCustomerDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
import com.example.alreadytalbt.User.model.Customer;

import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.repo.CustomerRepo;
import com.example.alreadytalbt.User.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private RestaurantClient restaurantClient;

    @Autowired
    private JwtUtil jwtUtil;

    public CustomerResponseDTO createCustomer(CreateCustomerDTO dto, String token) {
        String userId = jwtUtil.extractUserId(token);

        // Optional: check that user role is CUSTOMER
        User user = userRepo.findById(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("User is not a customer");
        }

        Customer customer = new Customer();
        customer.setUserId(new ObjectId(userId));
        customer.setOrderIds(dto.getOrderIds());

        customerRepo.save(customer);

        return mapToResponse(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public Optional<CustomerResponseDTO> getCustomerById(ObjectId id) {
        return customerRepo.findById(id).map(this::mapToResponse);
    }


    public Optional<Customer> updateCustomer(ObjectId id, UpdateCustomerDTO dto) {
        return customerRepo.findById(id).map(customer -> {
            customer.setOrderIds(dto.getOrderIds());
            return customerRepo.save(customer);
        });
    }

    public boolean deleteCustomer(ObjectId id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RestaurantResponseDTO> viewAllRestaurants() {
        return restaurantClient.getAllRestaurants();
    }

    private CustomerResponseDTO mapToResponse(Customer customer) {
        User user = userRepo.findById(customer.getUserId())
                .orElseThrow(() -> new RuntimeException(("User not found for customer")));

        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getId().toHexString());
        dto.setUserId(user.getId().toHexString());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setOrderIds(
                customer.getOrderIds() != null
                        ? customer.getOrderIds().stream()
                        .map(ObjectId::toHexString)
                        .toList()
                        : List.of()
        );
        dto.setPhone(user.getPhone());


        return dto;
    }

}
