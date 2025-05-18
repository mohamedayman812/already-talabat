// src/main/java/com/example/alreadytalbt/User/auth/controller/AuthController.java
package com.example.alreadytalbt.User.auth.controller;

import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.Notifications.Controller.NotificationController;
import com.example.alreadytalbt.User.dto.AuthUserResponseDTO;
import com.example.alreadytalbt.User.dto.UserResponseDTO;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.model.Vendor;
import com.example.alreadytalbt.User.repo.CustomerRepo;
import com.example.alreadytalbt.User.repo.DeliveryGuyRepo;
import com.example.alreadytalbt.Restaurant.Repository.RestaurantRepository;
import com.example.alreadytalbt.User.repo.UserRepo;
import com.example.alreadytalbt.User.repo.VendorRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepo userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private NotificationController notificationController;
    @Autowired private CustomerRepo customerRepo;
    @Autowired private VendorRepo vendorRepo;
    @Autowired private DeliveryGuyRepo deliveryGuyRepo;
    @Autowired private RestaurantRepository restaurantRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email    = body.get("email");
        String password = body.get("password");
        String address  = body.get("address");
        String phone    = body.get("phone");
        String roleStr  = body.get("role");

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The user already exists");
        }

        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid role provided.");
        }

        // 1) create base User
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashedPassword, address, phone, role);
        userRepository.save(user);

        // 2) create profile record(s)
        if (role == Role.CUSTOMER) {
            Customer c = new Customer();
            c.setUserId(user.getId());
            customerRepo.save(c);

        } else if (role == Role.VENDOR) {
            Vendor v = new Vendor();
            v.setUserId(user.getId());
            Vendor savedVendor = vendorRepo.save(v);

            Restaurant r = new Restaurant();
            r.setVendorId(savedVendor.getId());
            r.setName(body.get("restaurantName"));
            r.setAddress(body.get("restaurantAddress"));
            r.setMenuItems(Collections.emptyList());
            Restaurant savedRestaurant = restaurantRepo.save(r);

            savedVendor.setRestaurantId(savedRestaurant.getId());
            vendorRepo.save(savedVendor);

        } else if (role == Role.DELIVERY) {
            DeliveryGuy d = new DeliveryGuy();
            d.setUserId(user.getId());
            deliveryGuyRepo.save(d);
        }

        // 3) generate JWT + return AuthUserResponseDTO
        String token = jwtUtil.generateToken(user.getId());
        List<String> notificationIds = Collections.emptyList();
        AuthUserResponseDTO response =
                new AuthUserResponseDTO(username, email, role, notificationIds, token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
        User user = userOpt.get();

        String token = jwtUtil.generateToken(user.getId());
        AuthUserResponseDTO response = new AuthUserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getNotificationId(),
                token
        );

        if (response.getRole() == Role.CUSTOMER) {
            String custId = getCustomerIdFromUserId(user.getId().toHexString()).toHexString();
            notificationController.getNotificationsForCustomer(custId, response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Returns the currently authenticated user’s basic profile,
     * including their MongoDB ObjectId so the front-end can PUT /api/users/{id}.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userIdStr = jwtUtil.extractUserId(auth.substring(7));

        Optional<User> userOpt = userRepository.findById(new ObjectId(userIdStr));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User u = userOpt.get();

        UserResponseDTO dto = new UserResponseDTO(
                u.getId().toHexString(),
                u.getUsername(),
                u.getEmail(),
                u.getAddress(),
                u.getPhone(),
                u.getRole().name()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        String userIdStr = jwtUtil.extractUserId(auth.substring(7));

        Optional<User> userOpt = userRepository.findById(new ObjectId(userIdStr));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        User u = userOpt.get();

        String current = body.get("currentPassword");
        String next    = body.get("newPassword");
        if (!passwordEncoder.matches(current, u.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }

        u.setPassword(passwordEncoder.encode(next));
        userRepository.save(u);
        return ResponseEntity.ok("Password changed successfully");
    }

    private ObjectId getCustomerIdFromUserId(String userIdStr) {
        ObjectId userId;
        try {
            userId = new ObjectId(userIdStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid userId format: " + userIdStr);
        }
        return customerRepo.findByUserId(userId)
                .map(Customer::getId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found for userId: " + userIdStr)
                );
    }
}
