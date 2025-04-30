package com.example.alreadytalbt.User.repo;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.alreadytalbt.User.model.Vendor;

public interface VendorRepo extends MongoRepository<Vendor, String> {

}

