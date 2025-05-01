package com.example.alreadytalbt.Order.Model;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
public class Cart {
//we need the customer id to refrence the customer id in the customer table
    //the id must be object
    //it should refrece/hold the menuitems?
    @Id
    private ObjectId id;

    private ObjectId customerId;

    private List<MenuItemDTO> items;

    public Cart() {}

    public Cart(ObjectId customerId, List<MenuItemDTO> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }
}
