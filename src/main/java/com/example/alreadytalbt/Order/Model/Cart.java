package com.example.alreadytalbt.Order.Model;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
public class Cart {
    @Id
    private ObjectId id;

    private ObjectId customerId;

    private ObjectId restaurantId;

    private List<ObjectId> menuItemIds;

    public Cart() {}

    public Cart(ObjectId id, ObjectId customerId, ObjectId restaurantId, List<ObjectId> menuItemIds) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.menuItemIds = menuItemIds;
    }

    public ObjectId getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(ObjectId restaurantId) {
        this.restaurantId = restaurantId;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getCustomerId() { return customerId; }
    public void setCustomerId(ObjectId customerId) { this.customerId = customerId; }

    public List<ObjectId> getMenuItemIds() { return menuItemIds; }
    public void setMenuItemIds(List<ObjectId> menuItemIds) { this.menuItemIds = menuItemIds; }
}

