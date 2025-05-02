package com.example.alreadytalbt.Order.Service;

import com.example.alreadytalbt.Order.Model.Cart;
import com.example.alreadytalbt.Order.Repositories.CartRepo;
import com.example.alreadytalbt.Order.dto.*;
import com.example.alreadytalbt.Order.feign.RestrauntClient;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepository;

//    public CartDTO createCart(CreateCartDTO dto) {
//        Cart cart = new Cart();
//        cart.setCustomerId(new ObjectId(dto.getCustomerId()));
//
//        List<ObjectId> ids = dto.getMenuItemIds() != null
//                ? dto.getMenuItemIds().stream().map(ObjectId::new).toList()
//                : List.of();
//
//        cart.setMenuItemIds(ids);
//        return toDTO(cartRepository.save(cart));
//    }



    @Autowired
    private RestrauntClient menuItemClient;
    //add a menu item to a cart, the cart is created automatically in this fucntion if cart doesnt already exists
    public CartDTO addItemsToCart(AddToCartRequestDTO dto) {
        ObjectId customerObjId = new ObjectId(dto.getCustomerId());

        Cart cart = cartRepository.findByCustomerId(customerObjId);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerObjId);
            cart.setMenuItemIds(new ArrayList<>());
        }

        for (String menuItemId : dto.getMenuItemIds()) {
            //  Validate that the item exists
            try {
                menuItemClient.getMenuItemById(menuItemId); // if it throws 404, catch it
            } catch (Exception e) {
                throw new RuntimeException("Menu item does not exist: " + menuItemId);
            }

            ObjectId menuId = new ObjectId(menuItemId);
            if (!cart.getMenuItemIds().contains(menuId)) {
                cart.getMenuItemIds().add(menuId);
            }
        }

        return toDTO(cartRepository.save(cart));
    }


    //get cart with all the item details inside it
    public CartWithItemsDTO getCartByCustomerIdwithdetails(String customerId) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(customerId));
        if (cart == null) throw new RuntimeException("Cart not found");

        List<MenuItemDTO> fullItems = cart.getMenuItemIds().stream()
                .map(id -> menuItemClient.getMenuItemById(id.toHexString()))
                .toList();

        CartWithItemsDTO dto = new CartWithItemsDTO();
        dto.setId(cart.getId().toHexString());
        dto.setCustomerId(cart.getCustomerId().toHexString());
        dto.setItems(fullItems);
        return dto;
    }


    public CartDTO getCartWithIdsOnly(String customerId) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(customerId));
        if (cart == null) throw new RuntimeException("Cart not found");

        return toDTO(cart); // Reuses your existing helper method
    }


    //update a cart
    public CartDTO updateCart(String customerId, UpdateCartDTO dto) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(customerId));
        List<ObjectId> updatedIds = dto.getMenuItemIds() != null
                ? dto.getMenuItemIds().stream().map(ObjectId::new).toList()
                : List.of();
        cart.setMenuItemIds(updatedIds);
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
        dto.setMenuItemIds(cart.getMenuItemIds().stream()
                .map(ObjectId::toHexString)
                .toList());
        return dto;
    }

    public CartDTO removeItemFromCart(RemoveFromCartRequestDTO dto) {
        Cart cart = cartRepository.findByCustomerId(new ObjectId(dto.getCustomerId()));
        if (cart == null) throw new RuntimeException("Cart not found");

        ObjectId itemId = new ObjectId(dto.getMenuItemId());
        cart.getMenuItemIds().removeIf(id -> id.equals(itemId));

        return toDTO(cartRepository.save(cart));
    }



}
