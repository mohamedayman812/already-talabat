package com.example.alreadytalbt.Order.Controller;

import com.example.alreadytalbt.Order.dto.*;
import com.example.alreadytalbt.Order.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

//    @PostMapping
//    public ResponseEntity<CartDTO> createCart(@RequestBody CreateCartDTO dto) {
//        return ResponseEntity.ok(cartService.createCart(dto));
//    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartWithItemsDTO> getCartByCustomerIdwithdetails(@PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomerIdwithdetails(customerId));
    }

    @GetMapping("/{customerId}/ids-only")
    public ResponseEntity<CartDTO> getCartWithIdsOnly(@PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getCartWithIdsOnly(customerId));
    }


    @PutMapping("/{customerId}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable String customerId, @RequestBody UpdateCartDTO dto) {
        return ResponseEntity.ok(cartService.updateCart(customerId, dto));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String customerId) {
        cartService.deleteCart(customerId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/add-items")
    public ResponseEntity<CartDTO> addItemsToCart(@RequestBody AddToCartRequestDTO dto) {
        return ResponseEntity.ok(cartService.addItemsToCart(dto));
    }

    @PostMapping("/remove-item")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestBody RemoveFromCartRequestDTO dto) {
        return ResponseEntity.ok(cartService.removeItemFromCart(dto));
    }



}
