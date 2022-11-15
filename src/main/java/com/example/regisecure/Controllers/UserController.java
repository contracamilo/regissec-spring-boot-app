package com.example.regisecure.Controllers;

import com.example.regisecure.Models.RoleModel;
import com.example.regisecure.Models.UserModel;
import com.example.regisecure.Repositories.RoleRepository;
import com.example.regisecure.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserModel userinfo){
        try {
            userinfo.setPassword(convertSHA256(userinfo.getPassword()));
            UserModel savedUser = this.userRepo.save(userinfo);
            return new ResponseEntity<UserModel>(savedUser, HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        try {
            List<UserModel> users = this.userRepo.findAll();
            return new ResponseEntity<List<UserModel>>(users, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?>  getUserByID(@PathVariable String id){
        try{
            UserModel currentUser = this.userRepo.findById(id).orElse(null);
            return new ResponseEntity<UserModel>(currentUser, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody  UserModel userData){
        UserModel currentUser = this.userRepo.findById(id).orElse(null);
        if(currentUser == null){
            return new ResponseEntity<String>("User already exists", HttpStatus.ALREADY_REPORTED);
        }
        try {
            currentUser.setUsername(userData.getUsername());
            currentUser.setEmail(userData.getEmail());
            currentUser.setPassword(convertSHA256(userData.getPassword()));
            UserModel updatedUser = this.userRepo.save(currentUser);
            return new ResponseEntity<UserModel>(updatedUser, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        UserModel currentUser = this.userRepo.findById(id).orElse(null);
        if (currentUser == null){
            return new ResponseEntity<String>("User does not exits", HttpStatus.NO_CONTENT);
        }

        try{
            this.userRepo.delete(currentUser);
            return new ResponseEntity<String>("User deleted", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * (1 a n) relationship btw rol and user
     * @param id
     * @param id_rol
     * @return
     */
    @PutMapping("{id}/rol/{id_rol}")
    public UserModel assignRoleToUser(@PathVariable String id,@PathVariable String id_rol){
        UserModel currentUser = this.userRepo.findById(id).orElse(null);
        RoleModel currentRole = this.roleRepo.findById(id_rol).orElse(null);

        if (currentUser != null && currentRole != null){
            currentUser.setRol(currentRole);
            return this.userRepo.save(currentUser);
        }else{
            return null;
        }
    }

    @PostMapping("/validate")
    public UserModel validateUser(@RequestBody  UserModel userData, final HttpServletResponse response) throws IOException {
        UserModel currentUser = this.userRepo.getUserByEmail(userData.getEmail());
        if (currentUser != null &&
                currentUser.getPassword().equals(convertSHA256(userData.getPassword()))) {
            currentUser.setPassword("");
            return currentUser;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }

    public String convertSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
