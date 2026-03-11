package com.shehab.udms.DTO;

import com.shehab.udms.types.Role;

public record UserDTO(
        String username,
        Role role,
        String token
) {}