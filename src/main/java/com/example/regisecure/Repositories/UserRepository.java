package com.example.regisecure.Repositories;
import com.example.regisecure.Models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<UserModel, String> {
    @Query("{'email': ?0}")
    public UserModel getUserByEmail(String email);
}