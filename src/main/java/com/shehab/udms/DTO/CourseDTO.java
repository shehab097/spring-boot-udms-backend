package com.shehab.udms.DTO;

import com.shehab.udms.types.Department;

public record CourseDTO(
        Long id,
        String courseCode,
        String courseName,
        int courseSemester,
        double courseCredit,
        Department courseDepartment
) {}