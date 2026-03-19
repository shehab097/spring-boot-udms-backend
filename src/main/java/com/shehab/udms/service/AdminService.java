package com.shehab.udms.service;


import com.shehab.udms.DTO.AdminDTO;
import com.shehab.udms.model.Admin;
import com.shehab.udms.repo.AdminRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;

    // dto
    private static @NonNull AdminDTO getDto(Admin admin) {
        return new AdminDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getAddress(),
                admin.getGender(),
                admin.getUser().getId(),   // Extracting from nested Users
                admin.getUser().getRole()  // Extracting from nested Users
        );
    }

    public List<AdminDTO> getAllAdminDTOs() {
        return adminRepo.findAll().stream()
                .map(admin -> getDto(admin))
                .toList();
    }

    public AdminDTO getAdminDTO(String username){
        Admin admin = adminRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return getDto(admin);
    }

    public AdminDTO updateAdminDTO(String username, Admin updatedAdmin){
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

        adminRepo.save(admin);

        return getDto(admin);
    }

}
