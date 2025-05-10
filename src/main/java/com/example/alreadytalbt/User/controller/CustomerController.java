package com.example.alreadytalbt.User.controller;
import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.dto.*;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.auth.JwtUtil;
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
    @Autowired
    private JwtUtil jwtUtil;


    // CREATE
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerDTO dto,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            CustomerResponseDTO customer = customerService.createCustomer(dto, token);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
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
    public ResponseEntity<?> updateCustomer(@PathVariable ObjectId id,
                                            @RequestBody UpdateCustomerDTO dto,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            Customer updated = customerService.updateCustomer(id, dto, token)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable ObjectId id,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            boolean deleted = customerService.deleteCustomer(id, token);
            if (deleted) return ResponseEntity.noContent().build();
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }



    //RESTRAUNT RELATED STUFF
    @Autowired
    private RestaurantClient restaurantClient;

    //view all restraunts..it shows all the restraunts and the menu items inside it
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantResponseDTO>> viewAllRestaurants(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        // optionally validate token
        List<RestaurantResponseDTO> restaurants = restaurantClient.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }


    // view a specific restraunt
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable String restaurantId,
                                                                   @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        jwtUtil.extractUserId(token); // optional: to validate token
        return ResponseEntity.ok(restaurantClient.getRestaurantById(restaurantId));
    }

    //view a specific menu item
    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuItemDTO> menuitemById(@PathVariable String id,
                                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        jwtUtil.extractUserId(token); // optional: to validate token
        MenuItemDTO menuItem = restaurantClient.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }


    ///CART RELATED STUFF

    /// add item to a cart
    @Autowired
    private OrderFeignClient cartClient;
    @PostMapping("/cart/add-item")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody AddToCartRequestDTO request,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        // optionally validate customerId in request matches token
        CartDTO updatedCart = cartClient.addItemsToCart(request);
        return ResponseEntity.ok(updatedCart);
    }


    //Delete an item from a cart
    @PostMapping("/cart/remove-item")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestBody RemoveFromCartRequestDTO dto,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        // optionally validate customerId in dto matches token
        CartDTO updatedCart = cartClient.removeItemFromCart(dto);
        return ResponseEntity.ok(updatedCart);
    }


    //view a cart with its items details
    @GetMapping("/cart/{customerId}/details")
    public ResponseEntity<CartWithItemsDTO> getCartWithDetails(@PathVariable String customerId,
                                                               @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        if (!userId.equals(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CartWithItemsDTO cart = cartClient.getCartByCustomerIdwithdetails(customerId);
        return ResponseEntity.ok(cart);
    }



    //view cart wiht no items details
    @GetMapping("/cart/{customerId}/ids-only")
    public ResponseEntity<CartDTO> getCartWithIdsOnly(@PathVariable String customerId,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        if (!userId.equals(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CartDTO cart = cartClient.getCartWithIdsOnly(customerId);
        return ResponseEntity.ok(cart);
    }



    //delete a cart
    @DeleteMapping("/cart/{customerId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String customerId,
                                           @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        if (!userId.equals(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        cartClient.deleteCart(customerId);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/cart/submit/{cartId}/paymentMethod")
    public ResponseEntity<CreateOrderDTO> submitOrder(@PathVariable String cartId,
                                                      @RequestParam String paymentMethod,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        // optionally validate user before placing order
        CreateOrderDTO order = cartClient.submitOrder(cartId, paymentMethod);
        return ResponseEntity.ok(order);
    }





}
