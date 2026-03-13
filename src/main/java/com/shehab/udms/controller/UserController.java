package com.shehab.udms.controller;

import com.shehab.udms.DTO.UserDTO;
import com.shehab.udms.DTO.UserInfoDTO;
import com.shehab.udms.model.Users;
import com.shehab.udms.service.UserService;
import com.shehab.udms.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@EnableMethodSecurity
@RestController
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
//    public ResponseEntity<Users> register(@RequestBody Users user){
    public ResponseEntity<?> register(@RequestBody Users user){
        try{
            return ResponseEntity.ok(service.register(user));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
//    public ResponseEntity<UserDTO> login(@RequestBody Users user){
    public ResponseEntity<?> login(@RequestBody Users user){

        try {
            UserDTO dto = service.verify(user);
            return ResponseEntity.ok(dto);
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(e.getMessage())); // Unauthorized
        }
    }

    // get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDTO>> getUsers(){
        return ResponseEntity.ok(service.findAllUsers());
    }
}
