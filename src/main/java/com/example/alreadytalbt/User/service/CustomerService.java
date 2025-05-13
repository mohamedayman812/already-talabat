package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Order.dto.AddToCartRequestDTO;
import com.example.alreadytalbt.Order.dto.CartDTO;
import com.example.alreadytalbt.Order.dto.CartWithItemsDTO;
import com.example.alreadytalbt.Order.dto.RemoveFromCartRequestDTO;
import com.example.alreadytalbt.Restaurant.Service.RestaurantService;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.FeignClient.OrderFeignClient;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.dto.CreateCustomerDTO;
import com.example.alreadytalbt.User.dto.CustomerResponseDTO;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OrderFeignClient cartClient;
    ;

    public CustomerResponseDTO createCustomerFromToken(CreateCustomerDTO dto, String token) {
        String userId = jwtUtil.extractUserId(token);

        User user = userRepo.findById(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("User is not a customer");
        }

        // Check if already has customer entity
        if (customerRepo.findByUserId(new ObjectId(userId)).isPresent()) {
            throw new RuntimeException("Customer already exists");
        }

        Customer customer = new Customer();
        customer.setUserId(user.getId());
        customer.setOrderIds(dto.getOrderIds());

        customerRepo.save(customer);
        return mapToResponse(customer);
    }


    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public Optional<CustomerResponseDTO> getCustomerByUserId(String userId) {
        return customerRepo.findByUserId(new ObjectId(userId)).map(this::mapToResponse);
    }



    public Optional<Customer> updateCustomerFromToken(UpdateCustomerDTO dto, String token) {
        String userId = jwtUtil.extractUserId(token);

        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setOrderIds(dto.getOrderIds());
        return Optional.of(customerRepo.save(customer));
    }



    public boolean deleteCustomerFromToken(String token) {
        String userId = jwtUtil.extractUserId(token);

        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerRepo.deleteById(customer.getId());
        userRepo.deleteById(new ObjectId(userId)); // <--- add this line

        return true;
    }


    ///RESTAURANT RELATED STUFF
    public List<RestaurantResponseDTO> viewAllRestaurants(String token) {

        String userId = jwtUtil.extractUserId(token);
        return restaurantClient.getAllRestaurants(); // no auth required here
    }

    public RestaurantResponseDTO getRestaurantById(String restaurantId, String token) {
        String userId = jwtUtil.extractUserId(token);
        return restaurantClient.getRestaurantById(restaurantId);
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

    public CartDTO handleAddToCart(AddToCartRequestDTO request, String token) {
        String userId = jwtUtil.extractUserId(token); // Validate & extract user ID

        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));

        request.setCustomerId(customer.getId().toString()); // Inject real customer ID

        return cartClient.addItemsToCart(request); // No token needed
    }


    public CartDTO removeItemFromCart(RemoveFromCartRequestDTO request, String token) {
        String userId = jwtUtil.extractUserId(token);
        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));
        request.setCustomerId(customer.getId().toString());
        return cartClient.removeItemFromCart(request);
    }


    public RestaurantResponseDTO getRestaurantById(String restaurantId) {
        return restaurantClient.getRestaurantById(restaurantId); // No authHeader needed
    }

    public MenuItemDTO getMenuItemById(String menuItemId, String token) {
        String userId = jwtUtil.extractUserId(token); // Just to validate the token
        return restaurantClient.getMenuItemById(menuItemId);
    }


    public CartDTO getCartWithIdsOnly(String token) {
        String userId = jwtUtil.extractUserId(token);
        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));
        return cartClient.getCartWithIdsOnly(customer.getId().toString());
    }

    public CartWithItemsDTO getCartWithDetails(String token) {
        String userId = jwtUtil.extractUserId(token);
        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));
        return cartClient.getCartByCustomerIdwithdetails(customer.getId().toString());
    }

    
    public void handleDeleteCart(String token) {
        String userId = jwtUtil.extractUserId(token);
        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));
        cartClient.deleteCart(customer.getId().toString());
    }


}
