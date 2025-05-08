package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.model.User;
import com.example.alreadytalbt.User.dto.UserUpdateDTO;
import com.example.alreadytalbt.User.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import com.example.alreadytalbt.User.dto.UserRequestDTO;
import com.example.alreadytalbt.User.dto.UserResponseDTO;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        String username = dto.getEmail();
        Role role = Role.CUSTOMER;
        User user = new User(username,dto.getEmail(), dto.getPassword(), dto.getAddress(), dto.getPhone(), role);
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getAddress()
        );
    }


    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId().toString(), user.getUsername(), user.getEmail(), user.getAddress()))
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserById(ObjectId id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId().toString(), user.getUsername(), user.getEmail(), user.getAddress()));
    }

    public Optional<UserResponseDTO> updateUser(ObjectId id, UserUpdateDTO updateDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(updateDTO.getName());
            user.setEmail(updateDTO.getEmail());
            user.setPassword(updateDTO.getPassword());
            user.setAddress(updateDTO.getAddress());


            User updated = userRepository.save(user);

            return Optional.of(new UserResponseDTO(
                    updated.getId().toString(),
                    updated.getUsername(),
                    updated.getEmail(),
                    updated.getAddress()
            ));
        }

        return Optional.empty();
    }

    public boolean deleteUser(ObjectId id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
