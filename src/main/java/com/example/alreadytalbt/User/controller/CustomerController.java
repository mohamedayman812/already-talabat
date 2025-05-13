package com.example.alreadytalbt.User.controller;
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
import jakarta.validation.Valid;
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
    @Autowired
    private OrderFeignClient cartClient;
    @Autowired
    private RestaurantClient restaurantClient;


    // CREATE
    @PostMapping("/me")
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerDTO dto,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            CustomerResponseDTO customer = customerService.createCustomerFromToken(dto, token);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    // READ by ID
//    @GetMapping
//    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
//        return ResponseEntity.ok(customerService.getAllCustomers());
//    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponseDTO> getMyCustomerProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        return customerService.getCustomerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/me")
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateCustomerDTO dto,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            Customer updated = customerService.updateCustomerFromToken(dto, token)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            boolean deleted = customerService.deleteCustomerFromToken(token);
            if (deleted) return ResponseEntity.noContent().build();
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

    //RESTRAUNT RELATED STUFF


    //view all restraunts. it shows all the restraunts and the menu items inside it
    //tested
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantResponseDTO>> viewAllRestaurants(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        return ResponseEntity.ok(customerService.viewAllRestaurants(token));
    }


    // view a specific restraunt
    //tested
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(
            @PathVariable String restaurantId,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        return ResponseEntity.ok(customerService.getRestaurantById(restaurantId,  token));
    }


    //view a specific menu item
    //tested
    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuItemDTO> menuitemById(@PathVariable String id,
                                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        return ResponseEntity.ok(customerService.getMenuItemById(id, token));
    }


    ///CART RELATED STUFF
    /// add item to a cart
    /// tested
    @PostMapping("/cart/add-item")
    public ResponseEntity<CartDTO> addItemToCart(@Valid @RequestBody AddToCartRequestDTO request,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        CartDTO updatedCart = customerService.handleAddToCart(request, token);
        return ResponseEntity.ok(updatedCart);
    }

    //Delete an item from a cart
    //
    @PostMapping("/cart/remove-item")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestBody RemoveFromCartRequestDTO request,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        CartDTO updatedCart = customerService.removeItemFromCart(request, token);
        return ResponseEntity.ok(updatedCart);
    }

    //view a cart with its items details
    @GetMapping("/cart/ids-only")
    public ResponseEntity<CartDTO> getCartWithIdsOnly(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(customerService.getCartWithIdsOnly(token));
    }

    @GetMapping("/cart/details")
    public ResponseEntity<CartWithItemsDTO> getCartWithDetails(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(customerService.getCartWithDetails(token ));
    }

    //delete cart
    @DeleteMapping("/cart/remove")
    public ResponseEntity<Void> deleteCart(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        customerService.handleDeleteCart(token);
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
