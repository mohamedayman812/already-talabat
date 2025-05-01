package com.example.alreadytalbt.Order.Repositories;

import com.example.alreadytalbt.Order.Model.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepo extends MongoRepository<Cart, ObjectId> {
    Cart findByCustomerId(ObjectId customerId);
}
