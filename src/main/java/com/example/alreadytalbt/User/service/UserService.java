package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.dto.UserUpdateDTO;
import com.example.alreadytalbt.User.dto.UserRequestDTO;
import com.example.alreadytalbt.User.dto.UserResponseDTO;
import com.example.alreadytalbt.User.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    /**
     * Creates a new user (default CUSTOMER role) based on the incoming request DTO.
     */
    public UserResponseDTO createUser(UserRequestDTO dto) {
        Role role = Role.CUSTOMER;
        User user = new User(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getAddress(),
                dto.getPhone(),
                role
        );
        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId().toHexString(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getAddress(),
                saved.getPhone(),
                saved.getRole().name()
        );
    }

    /**
     * Retrieves all users and maps them to response DTOs.
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponseDTO(
                        u.getId().toHexString(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAddress(),
                        u.getPhone(),
                        u.getRole().name()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single user by ID.
     */
    public Optional<UserResponseDTO> getUserById(ObjectId id) {
        return userRepository.findById(id)
                .map(u -> new UserResponseDTO(
                        u.getId().toHexString(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAddress(),
                        u.getPhone(),
                        u.getRole().name()
                ));
    }

    /**
     * Updates the profile fields of an existing user (username, email, address, phone).
     * Password changes are handled via /api/auth/change-password instead.
     */
    public Optional<UserResponseDTO> updateUser(ObjectId id, UserUpdateDTO dto) {
        return userRepository.findById(id)
                .map(u -> {
                    u.setUsername(dto.getUsername());
                    u.setEmail(dto.getEmail());
                    u.setAddress(dto.getAddress());
                    u.setPhone(dto.getPhone());

                    User updated = userRepository.save(u);
                    return new UserResponseDTO(
                            updated.getId().toHexString(),
                            updated.getUsername(),
                            updated.getEmail(),
                            updated.getAddress(),
                            updated.getPhone(),
                            updated.getRole().name()
                    );
                });
    }

    /**
     * Deletes a user by ID, returning true if the user existed.
     */
    public boolean deleteUser(ObjectId id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
