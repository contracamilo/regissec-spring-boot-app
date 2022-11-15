package com.example.regisecure.Repositories;

import com.example.regisecure.Models.PermissionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRepository extends MongoRepository<PermissionModel, String> {
    @Query("{'url':?0,'method':?1}")
    PermissionModel getPermission(String url, String method);
}
