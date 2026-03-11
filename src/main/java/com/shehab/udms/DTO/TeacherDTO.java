package com.shehab.udms.DTO;

import com.shehab.udms.types.Gender;
import com.shehab.udms.types.Role;

public record TeacherDTO(
        Long id,
        String username,
        String name,
        String email,
        String phone,
        String address,
        Gender gender,
        Long userId,    // From nested Users
        Role role       // From nested Users
) {}