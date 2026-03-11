package com.shehab.udms.DTO;

import com.shehab.udms.types.Gender;
import com.shehab.udms.types.Role;

public record AdminDTO(
        Long id,
        String username,
        String name,
        String email,
        String phone,
        String address,
        Gender gender,
        Long userId,    // From the OneToOne User relationship
        Role role       // From the OneToOne User relationship
) {}