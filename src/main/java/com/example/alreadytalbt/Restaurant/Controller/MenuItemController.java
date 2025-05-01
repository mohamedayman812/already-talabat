package com.example.alreadytalbt.Restaurant.Controller;


import com.example.alreadytalbt.Restaurant.Service.MenuItemService;
import com.example.alreadytalbt.Restaurant.dto.MenuItemCreateDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemDTO> addMenuItem(@RequestBody MenuItemCreateDTO dto) {
        MenuItemDTO created = menuItemService.addMenuItem(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable String menuItemId, @RequestBody MenuItemUpdateDTO dto) {
        MenuItemDTO updated = menuItemService.updateMenuItem(menuItemId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable String menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
        return ResponseEntity.ok("Menu item deleted successfully.");
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable String menuItemId) {
        return menuItemService.getMenuItemById(menuItemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

