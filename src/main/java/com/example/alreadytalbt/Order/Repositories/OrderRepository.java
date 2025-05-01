package com.example.alreadytalbt.Order.Repositories;

import com.example.alreadytalbt.Order.Model.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findByDeliveryGuyId(ObjectId deliveryGuyId);
    List<Order> findByCustomerId(ObjectId customerId);

}
