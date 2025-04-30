package com.example.alreadytalbt.User.repo;


import com.example.alreadytalbt.User.model.Vendor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorRepo extends MongoRepository<Vendor, ObjectId> {}


