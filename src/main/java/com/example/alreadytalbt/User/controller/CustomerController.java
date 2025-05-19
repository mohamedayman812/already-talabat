package com.example.alreadytalbt.User.controller;
import com.example.alreadytalbt.Order.dto.*;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.auth.RequireAuthentication;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
import com.example.alreadytalbt.User.dto.UpdateCustomerDTO;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
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
    @RequireAuthentication
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerDTO dto,
                                           HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        try {
            CustomerResponseDTO customer = customerService.createCustomerFromToken(dto, userId);
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
    @RequireAuthentication
    public ResponseEntity<CustomerResponseDTO> getMyCustomerProfile(HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        return customerService.getCustomerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/me")
    @RequireAuthentication
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateCustomerDTO dto,
                                           HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        try {
            Customer updated = customerService.updateCustomerFromToken(dto, userId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/me")
    @RequireAuthentication
    public ResponseEntity<?> deleteCustomer(HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        try {
            boolean deleted = customerService.deleteCustomerFromToken(userId);
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
    @RequireAuthentication
    public ResponseEntity<List<RestaurantResponseDTO>> viewAllRestaurants(
           HttpServletRequest request) {
        //String token = authHeader.replace("Bearer ", "").trim();
       // String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(customerService.viewAllRestaurants());
    }


    // view a specific restraunt
    //tested
    @GetMapping("/restaurant/{restaurantId}")
    @RequireAuthentication
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(
            @PathVariable String restaurantId,
           HttpServletRequest request) {
        //String token = authHeader.replace("Bearer ", "").trim();
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(customerService.getRestaurantById(restaurantId,  userId));
    }


    //view a specific menu item
    //tested
    @GetMapping("/menu/{id}")
    @RequireAuthentication
    public ResponseEntity<MenuItemDTO> menuitemById(@PathVariable String id,
                                                   HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "").trim();
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(customerService.getMenuItemById(id));
    }

    @PostMapping("/cart/add-item")
    @RequireAuthentication
    public ResponseEntity<CartDTO> addItemToCart(@Valid @RequestBody AddToCartRequestDTO req,
                                                HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        CartDTO updatedCart = customerService.handleAddToCart(req, userId);
        return ResponseEntity.ok(updatedCart);
    }

    //Delete an item from a cart
    //
    @PostMapping("/cart/remove-item")
    @RequireAuthentication
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestBody RemoveFromCartRequestDTO req,
                                                     HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        CartDTO updatedCart = customerService.removeItemFromCart(req, userId);
        return ResponseEntity.ok(updatedCart);
    }

    //view a cart with its items details
    @GetMapping("/cart/ids-only")
    @RequireAuthentication
    public ResponseEntity<CartDTO> getCartWithIdsOnly( HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(customerService.getCartWithIdsOnly(userId));
    }

    @GetMapping("/cart/details")
    @RequireAuthentication
    public ResponseEntity<CartWithItemsDTO> getCartWithDetails( HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(customerService.getCartWithDetails(userId ));
    }

    //delete cart
    @DeleteMapping("/cart/remove")
    @RequireAuthentication
    public ResponseEntity<Void> deleteCart(HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        customerService.handleDeleteCart(userId);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/cart/submit/{cartId}/paymentMethod")
    @RequireAuthentication
    public ResponseEntity<CreateOrderDTO> submitOrder(@PathVariable String cartId,
                                                      @RequestParam String paymentMethod,
                                                     HttpServletRequest request) {
       // String token = authHeader.replace("Bearer ", "");
        // optionally validate user before placing order
        CreateOrderDTO order = cartClient.submitOrder(cartId, paymentMethod);
        return ResponseEntity.ok(order);
    }


}
