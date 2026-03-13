package com.shehab.udms.DTO;

import com.shehab.udms.types.Role;

public record UserInfoDTO(
        String username,
        Role role
) {
}
