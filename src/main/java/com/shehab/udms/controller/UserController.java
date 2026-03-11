package com.shehab.udms.controller;

import com.shehab.udms.DTO.UserDTO;
import com.shehab.udms.model.Users;
import com.shehab.udms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody Users user){

        try {
            UserDTO dto = service.verify(user);
            return ResponseEntity.ok(dto);
        }
        catch (Exception e) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }
}
