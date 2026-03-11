package com.shehab.udms.controller;


import com.shehab.udms.model.Admin;
import com.shehab.udms.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepo adminRepo;

    @GetMapping
    public List<Admin> getAllTeachers(){
        return adminRepo.findAll();
    }

    @GetMapping("/{username}")
    public Admin getAdminByUsername(@PathVariable String username) {
        return adminRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @PutMapping("/{username}")
    public Admin updateAdmin(@PathVariable String username, @RequestBody Admin updatedAdmin) {

        Admin admin = adminRepo.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Admin not found"));

        // get the logged-in username from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        System.out.println(loggedUsername + "##########");

        if (!loggedUsername.equals(username)) {
            throw new RuntimeException("You cannot update another admins's profile");
        }

        admin.setName(updatedAdmin.getName());
        admin.setEmail(updatedAdmin.getEmail());
        admin.setPhone(updatedAdmin.getPhone());
        admin.setAddress(updatedAdmin.getAddress());
        admin.setGender(updatedAdmin.getGender());

        return adminRepo.save(admin);
    }

}
