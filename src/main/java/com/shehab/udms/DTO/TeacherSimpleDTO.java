package com.shehab.udms.DTO;

import com.shehab.udms.types.Gender;
import com.shehab.udms.types.Role;

public record TeacherSimpleDTO(
        Long id,
        String username,
        String name,
        String email,
        Long userId,
        Role role
) {
}
