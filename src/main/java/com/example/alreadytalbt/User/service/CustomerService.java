package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.UpdateCustomerDTO;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.model.User;
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

        User savedUser = userRepo.save(user);

        // Create and save Customer with reference to User
        Customer customer = new Customer();
        customer.setUserId(savedUser.getId());
        customer.setOrderIds(dto.getOrderIds());

        return customerRepo.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Optional<Customer> getCustomerById(ObjectId id) {
        return customerRepo.findById(id);
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

    public List<RestaurantDTO> viewAllRestaurants() {
        return restaurantClient.getAllRestaurants();
    }
}
