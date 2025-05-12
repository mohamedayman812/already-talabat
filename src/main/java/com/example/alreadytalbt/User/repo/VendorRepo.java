package com.example.alreadytalbt.User.repo;


import com.example.alreadytalbt.User.model.Vendor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface VendorRepo extends MongoRepository<Vendor, ObjectId> {
    Vendor findByUserId(ObjectId userId);
}


