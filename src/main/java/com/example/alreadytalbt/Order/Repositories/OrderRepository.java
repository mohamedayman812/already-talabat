package com.example.alreadytalbt.Order.Repositories;

import com.example.alreadytalbt.Order.Model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
