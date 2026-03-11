package com.shehab.udms.DTO;

import com.shehab.udms.types.Department;
import com.shehab.udms.types.Gender;
import com.shehab.udms.types.Role;


public record StudentDTO(
        Long id,
        String username,
        String studentID,
        String name,
        String email,
        String phone,
        String address,
        Department department,
        Gender gender,
        Long userId,    // From nested Users
        Role role       // From nested Users
) {}