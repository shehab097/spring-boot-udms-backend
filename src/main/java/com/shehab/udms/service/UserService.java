package com.shehab.udms.service;

import com.shehab.udms.model.Users;
import com.shehab.udms.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); // we can pass straight

    public Users register(Users user){
        // before sending data to the database, we can encrypt password
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
