package com.example.regisecure.Repositories;

import com.example.regisecure.Models.PermissionRoleModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRoleRepository extends MongoRepository<PermissionRoleModel, String> {
    @Query("{'role.$id': ObjectId(?0),'permission.$id': ObjectId(?1)}")
    PermissionRoleModel getPermissionRole(String id_role,String id_permission);
}
