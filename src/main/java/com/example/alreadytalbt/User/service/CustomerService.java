package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
import com.example.alreadytalbt.User.dto.UpdateCustomerDTO;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.model.User;
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

    public Customer createCustomer(CreateCustomerDTO dto) {
        // Create and save base user
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setAddress(dto.getAddress());
        user.setRole(Role.CUSTOMER);
        user.setName("Default");
        user.setAddress("Unknown");
        user.setPhone("0000000000");
        User savedUser = userRepo.save(user);

        // Create and save Customer with reference to User
        Customer customer = new Customer();
        customer.setUserId(savedUser.getId());
        customer.setOrderIds(dto.getOrderIds());

        return customerRepo.save(customer);
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
                .orElseThrow(() -> new RuntimeException("User not found for customer"));

        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getId().toHexString());
        dto.setUserId(user.getId().toHexString());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setOrderIds(
                customer.getOrderIds() != null
                        ? customer.getOrderIds().stream()
                        .map(ObjectId::toHexString)
                        .toList()
                        : List.of()
        );


        return dto;
    }

}
