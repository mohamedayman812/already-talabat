package com.example.alreadytalbt.User.controller;
import com.example.alreadytalbt.Order.dto.AddToCartRequestDTO;
import com.example.alreadytalbt.Order.dto.CartDTO;
import com.example.alreadytalbt.Order.dto.CartWithItemsDTO;
import com.example.alreadytalbt.Order.dto.RemoveFromCartRequestDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
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
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable ObjectId id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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


    //RESTRAUNT RELATED STUFF
    @Autowired
    private RestaurantClient restaurantClient;

    //view all restraunts..it shows all the restraunts and the menu items inside it
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantResponseDTO>> viewAllRestaurants() {
        List<RestaurantResponseDTO> restaurants = restaurantClient.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }


     // view a specific restraunt
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable String restaurantId) {
        return ResponseEntity.ok(restaurantClient.getRestaurantById(restaurantId));
    }
    //view a specific menu item
    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuItemDTO> menuitemById(@PathVariable String id) {
        MenuItemDTO menuItem = restaurantClient.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    ///CART RELATED STUFF

    /// add item to a cart
    @Autowired
    private OrderFeignClient cartClient;
    @PostMapping("/cart/add-item")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody AddToCartRequestDTO request) {
        CartDTO updatedCart = cartClient.addItemsToCart(request);
        return ResponseEntity.ok(updatedCart);
    }

    //Delete an item from a cart
    @PostMapping("/cart/remove-item")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestBody RemoveFromCartRequestDTO dto) {
        CartDTO updatedCart = cartClient.removeItemFromCart(dto);
        return ResponseEntity.ok(updatedCart);
    }


    //view a cart with its items details
    @GetMapping("/cart/{customerId}/details")
    public ResponseEntity<CartWithItemsDTO> getCartWithDetails(@PathVariable String customerId) {
        CartWithItemsDTO cart = cartClient.getCartByCustomerIdwithdetails(customerId);
        return ResponseEntity.ok(cart);
    }


    //view cart wiht no items details
    @GetMapping("/cart/{customerId}/ids-only")
    public ResponseEntity<CartDTO> getCartWithIdsOnly(@PathVariable String customerId) {
        CartDTO cart = cartClient.getCartWithIdsOnly(customerId);
        return ResponseEntity.ok(cart);
    }


    //delete a cart
    @DeleteMapping("/cart/{customerId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String customerId) {
        cartClient.deleteCart(customerId);
        return ResponseEntity.noContent().build();
    }





}
