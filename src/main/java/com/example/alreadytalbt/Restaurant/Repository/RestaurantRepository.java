package com.example.alreadytalbt.Restaurant.Repository;

import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}

