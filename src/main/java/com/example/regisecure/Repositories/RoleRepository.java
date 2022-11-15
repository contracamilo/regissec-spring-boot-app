package com.example.regisecure.Repositories;

import com.example.regisecure.Models.RoleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<RoleModel, String> {
}
