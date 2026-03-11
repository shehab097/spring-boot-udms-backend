package com.shehab.udms.controller;


import com.shehab.udms.DTO.AdminDTO;
import com.shehab.udms.model.Admin;
import com.shehab.udms.repo.AdminRepo;
import com.shehab.udms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * only admin can access admins data
 */

@RestController
@EnableMethodSecurity
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private AdminService adminService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminDTO>> getAllTeachers() {
        List<AdminDTO> admins = adminService.getAllAdminDTOs();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AdminDTO> getAdminByUsername(@PathVariable String username) {

        AdminDTO dto = adminService.getAdminDTO(username);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{username}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable String username, @RequestBody Admin updatedAdmin) {

        AdminDTO dto = adminService.updateAdminDTO(username, updatedAdmin);
        return ResponseEntity.ok(dto);
    }

}
