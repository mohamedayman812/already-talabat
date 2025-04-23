package com.example.alreadytalbt.Restaurant.Service;

import com.example.alreadytalbt.Restaurant.Repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepo;

    public void deleteMenuItem(String id) {
        if (!menuItemRepo.existsById(id)) {
            throw new RuntimeException("MenuItem not found");
        }
        menuItemRepo.deleteById(id);
    }
}

