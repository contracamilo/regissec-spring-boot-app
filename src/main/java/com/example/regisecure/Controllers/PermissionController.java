package com.example.regisecure.Controllers;


import com.example.regisecure.Models.PermissionModel;
import com.example.regisecure.Models.PermissionModel;
import com.example.regisecure.Repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionRepository PermissionRepo;

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody PermissionModel data){
        try {
            PermissionModel savedPermission = this.PermissionRepo.save(data);
            return new ResponseEntity<PermissionModel>(savedPermission, HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPermissions(){
        try {
            List<PermissionModel> permissions = this.PermissionRepo.findAll();
            return new ResponseEntity<List<PermissionModel>>(permissions, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?>  getPermissionByID(@PathVariable String id){
        try{
            PermissionModel currentPermission = this.PermissionRepo.findById(id).orElse(null);
            return new ResponseEntity<PermissionModel>(currentPermission, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePermission(@PathVariable String id, @RequestBody  PermissionModel data){
        PermissionModel currentPermission = this.PermissionRepo.findById(id).orElse(null);
        if(currentPermission == null){
            return new ResponseEntity<String>("Role already exists", HttpStatus.ALREADY_REPORTED);
        }
        try {
            currentPermission.setUrl(data.getUrl());
            currentPermission.setMethod(data.getMethod());

            PermissionModel updatedPermission = this.PermissionRepo.save(currentPermission);
            return new ResponseEntity<PermissionModel>(updatedPermission, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePermission(@PathVariable String id){
        PermissionModel currentPermission = this.PermissionRepo.findById(id).orElse(null);
        if (currentPermission == null){
            return new ResponseEntity<String>("Permission does not exits", HttpStatus.NO_CONTENT);
        }

        try{
            this.PermissionRepo.delete(currentPermission);
            return new ResponseEntity<String>("Permission deleted", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
