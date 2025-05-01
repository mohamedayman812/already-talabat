package com.example.alreadytalbt.Order.Service;

import com.example.alreadytalbt.Order.Model.Cart;
import com.example.alreadytalbt.Order.Repositories.CartRepo;
import com.example.alreadytalbt.Order.dto.CartDTO;
import com.example.alreadytalbt.Order.dto.CreateCartDTO;
import com.example.alreadytalbt.Order.dto.UpdateCartDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public CartDTO createCart(CreateCartDTO dto) {
        Cart cart = new Cart();
        cart.setCustomerId(new ObjectId(dto.getCustomerId()));
        cart.setItems(dto.getItems() != null ? dto.getItems() : List.of());

        return toDTO(cartRepository.save(cart));
    }

    public CartDTO getCartByCustomerId(String customerId) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(customerId));
        return toDTO(cart);
    }

    public CartDTO updateCart(String customerId, UpdateCartDTO dto) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(customerId));
        cart.setItems(dto.getItems() != null ? dto.getItems() : List.of());
        return toDTO(cartRepository.save(cart));
    }

    public void deleteCart(String customerId) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(customerId));
        cartRepository.delete(cart);
    }

    private CartDTO toDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId().toHexString());
        dto.setCustomerId(cart.getCustomerId().toHexString());
        dto.setItems(cart.getItems());
        return dto;
    }
}
