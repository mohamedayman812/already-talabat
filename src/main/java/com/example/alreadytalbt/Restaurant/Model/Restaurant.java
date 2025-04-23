package com.example.alreadytalbt.Restaurant.Model;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import jakarta.validation.constraints.*;


@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;
    private String name;
    private String address;
    private List<MenuItem> menuItems;
    private String vendorId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


}
