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
        jwtUtil.extractUserId(token); // just to confirm it's a valid token
        return ResponseEntity.ok(restaurantClient.getRestaurantById(restaurantId));
    }

    //NOT WORKING IDK HWYYY  12/5
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

    @PostMapping("/cart/add-item")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody AddToCartRequestDTO request,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);

        // Inject customerId from token
        request.setCustomerId(userId);

        CartDTO updatedCart = cartClient.addItemsToCart(request,authHeader);
        return ResponseEntity.ok(updatedCart);
    }

    //Delete an item from a cart
    @PostMapping("/cart/remove-item")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestBody RemoveFromCartRequestDTO dto,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        // Inject customerId from token
        dto.setCustomerId(userId);
        CartDTO updatedCart = cartClient.removeItemFromCart(dto,authHeader);
        return ResponseEntity.ok(updatedCart);
    }

    //view a cart with its items details
    @GetMapping("/cart/details")
    public ResponseEntity<CartWithItemsDTO> getCartWithDetails(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        CartWithItemsDTO cart = cartClient.getCartByCustomerIdwithdetails(userId);
        return ResponseEntity.ok(cart);
    }

    //view cart wiht no items details
    @GetMapping("/cart/ids-only")
    public ResponseEntity<CartDTO> getCartWithIdsOnly(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        CartDTO cart = cartClient.getCartWithIdsOnly(userId);
        return ResponseEntity.ok(cart);
    }

    //delete a cart
    @DeleteMapping("/cart")
    public ResponseEntity<Void> deleteCart(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        cartClient.deleteCart(userId);
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
