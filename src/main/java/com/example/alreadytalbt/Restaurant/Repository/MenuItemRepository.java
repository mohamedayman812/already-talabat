package com.example.alreadytalbt.Restaurant.Repository;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuItemRepository extends MongoRepository<MenuItem, ObjectId> { }
