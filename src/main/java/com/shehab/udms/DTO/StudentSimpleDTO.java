package com.shehab.udms.DTO;

import com.shehab.udms.types.Department;
import com.shehab.udms.types.Gender;
import com.shehab.udms.types.Role;

public record StudentSimpleDTO(
        Long id,
        String username,
        String studentID,
        String name,
        Department department
) {
}
