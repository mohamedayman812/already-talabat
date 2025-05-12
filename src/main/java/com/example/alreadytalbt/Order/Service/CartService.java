package com.example.alreadytalbt.Order.Service;

import com.example.alreadytalbt.Order.Model.Cart;
import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Repositories.CartRepo;
import com.example.alreadytalbt.Order.dto.*;
import com.example.alreadytalbt.Order.feign.RestrauntClient;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.model.Customer;
import com.example.alreadytalbt.User.repo.CustomerRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepository;
    @Autowired
    private OrderService orderService;

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


    private JwtUtil jwtUtil;

    @Autowired
    private CustomerRepo customerRepo;

    public CartDTO addItemsToCart(AddToCartRequestDTO dto, String token) {
        // Extract userId from token
        String userId = jwtUtil.extractUserId(token);

        // Find the corresponding customer
        Customer customer = customerRepo.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));

        ObjectId customerId = customer.getId();
        ObjectId restaurantId;

        try {
            restaurantId = new ObjectId(dto.getRestaurantId());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid restaurantId format");
        }

        // Fetch or create cart
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setMenuItemIds(new ArrayList<>());
            cart.setRestaurantId(restaurantId);
        }

        // Add menu items
        for (String menuItemIdStr : dto.getMenuItemIds()) {
            ObjectId menuItemId;

            try {
                menuItemClient.getMenuItemById(menuItemIdStr); // validate item exists
                menuItemId = new ObjectId(menuItemIdStr);
            } catch (Exception ex) {
                throw new RuntimeException("Invalid menu item: " + menuItemIdStr);
            }

            if (cart.getMenuItemIds().contains(menuItemId)) {
                throw new RuntimeException("Item already in cart: " + menuItemIdStr);
            }

            cart.getMenuItemIds().add(menuItemId);
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
        System.out.println("ana f delete service ");
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
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + dto.getCustomerId());
        }

        ObjectId itemId = new ObjectId(dto.getMenuItemId());

        if (!cart.getMenuItemIds().contains(itemId)) {
            throw new RuntimeException("Item not found in cart: " + dto.getMenuItemId());
        }

        cart.getMenuItemIds().remove(itemId);

        return toDTO(cartRepository.save(cart));
    }


    public CreateOrderDTO submitOrder(String cartId, String paymentMethod) {
        ObjectId cartIdObject = new ObjectId(cartId);
        Cart cart = cartRepository.findById(cartIdObject)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getMenuItemIds() == null || cart.getMenuItemIds().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot submit order.");
        }

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setCustomerId(cart.getCustomerId());
        dto.setRestaurantId(cart.getRestaurantId());
        dto.setDeliveryGuyId(null);
        dto.setItems(cart.getMenuItemIds());
        dto.setStatus("PLACED");
        dto.setPaymentMethod(paymentMethod);

        orderService.createOrder(dto);
        return  dto;
    }


}
