package com.example.regisecure.Controllers;

import com.example.regisecure.Models.RoleModel;
import com.example.regisecure.Models.UserModel;
import com.example.regisecure.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleRepository roleRepo;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleModel modelInfo){
        try {
            RoleModel savedRole = this.roleRepo.save(modelInfo);
            return new ResponseEntity<RoleModel>(savedRole, HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles(){
        try {
            List<RoleModel> roles = this.roleRepo.findAll();
            return new ResponseEntity<List<RoleModel>>(roles, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?>  getRoleByID(@PathVariable String id){
        try{
            RoleModel currentRole = this.roleRepo.findById(id).orElse(null);
            return new ResponseEntity<RoleModel>(currentRole, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody  RoleModel roleData){
        RoleModel currentRole = this.roleRepo.findById(id).orElse(null);
        if(currentRole == null){
            return new ResponseEntity<String>("Role already exists", HttpStatus.ALREADY_REPORTED);
        }
        try {
            currentRole.setName(roleData.getName());
            currentRole.setDescription(roleData.getDescription());

            RoleModel updatedRole = this.roleRepo.save(currentRole);
            return new ResponseEntity<RoleModel>(updatedRole, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        RoleModel currentRole = this.roleRepo.findById(id).orElse(null);
        if (currentRole == null){
            return new ResponseEntity<String>("Role does not exits", HttpStatus.NO_CONTENT);
        }

        try{
            this.roleRepo.delete(currentRole);
            return new ResponseEntity<String>("Role deleted", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
