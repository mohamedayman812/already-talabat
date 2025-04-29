package com.example.alreadytalbt.User.service;

import com.example.alreadytalbt.Restaurant.dto.RestaurantUpdateDto;
import com.example.alreadytalbt.User.FeignClient.RestaurantClient;
import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.dto.VendorDTO;
import com.example.alreadytalbt.User.model.Vendor;
import com.example.alreadytalbt.User.repo.UserRepo;
import com.example.alreadytalbt.User.repo.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    private VendorRepo vendorRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestaurantClient restaurantClient;

    public VendorDTO createVendor(VendorDTO dto) {
        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setEmail(dto.getEmail());
        vendor.setRole(Role.VENDOR);
        vendor.setAddress(dto.getAddress());
        vendor.setPassword(dto.getPassword());
        userRepo.save(vendor);

        // Create restaurant with the vendorId
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setName(dto.getRestaurantName());
        restaurantDTO.setAddress(dto.getAddress());
        restaurantDTO.setVendorId(vendor.getId());


        RestaurantDTO createdRestaurant = restaurantClient.createRestaurant(restaurantDTO);
        vendor.setRestaurantId(createdRestaurant.getId());

        vendorRepository.save(vendor);

        return mapToDTO(vendor);
    }

    public RestaurantDTO getVendorRestaurant(String restaurantId) {
        return restaurantClient.getRestaurantById(restaurantId);
    }

    public VendorDTO getVendorById(String id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return mapToDTO(vendor);
    }

    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



    public VendorDTO updateVendor(String id, VendorDTO dto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Update vendor fields
        vendor.setName(dto.getName());
        vendor.setEmail(dto.getEmail());
        vendor.setAddress(dto.getAddress());
        vendor.setPassword(dto.getPassword());

        // Update restaurant info via Feign
        if (vendor.getRestaurantId() != null) {
            RestaurantUpdateDto restaurantUpdateDTO = new RestaurantUpdateDto();
            restaurantUpdateDTO.setName(dto.getRestaurantName());
            restaurantUpdateDTO.setAddress(dto.getRestaurantAddress());


            restaurantClient.updateRestaurant(vendor.getRestaurantId(), restaurantUpdateDTO);
        }
        userRepo.save(vendor);
        return mapToDTO(vendorRepository.save(vendor));
    }

    public void deleteVendor(String id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Delete the restaurant if exists
        if (vendor.getRestaurantId() != null) {
            try {
                restaurantClient.deleteRestaurant(vendor.getRestaurantId());
            } catch (Exception e) {
                // Log the error or rethrow if needed
                System.out.println("Failed to delete restaurant: " + e.getMessage());
            }
        }
        userRepo.deleteById(id);
        vendorRepository.deleteById(id);
    }

    private VendorDTO mapToDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setEmail(vendor.getEmail());
        dto.setPassword(vendor.getPassword());
        dto.setAddress(vendor.getAddress());
        dto.setRestaurantId(vendor.getRestaurantId());
        return dto;
    }
}
