package com.example.alreadytalbt.Order.Repositories;

import com.example.alreadytalbt.Order.Model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByDeliveryGuyId(String deliveryGuyId);
}
