package com.example.alreadytalbt.User.controller;

import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.UpdateCustomerDTO;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.service.CustomerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // CREATE
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerDTO dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable ObjectId id) {
        return customerService.getCustomerById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable ObjectId id, @RequestBody UpdateCustomerDTO dto) {
        return customerService.updateCustomer(id, dto)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable ObjectId id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private RestaurantClient restaurantClient;

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDTO>> viewAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantClient.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }
}
