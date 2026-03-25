package com.shehab.udms.DTO;

import com.shehab.udms.types.Department;
import com.shehab.udms.types.Gender;
import com.shehab.udms.types.Role;

public record CourseSimpleDTO( // will use inside teacher
        Long id,
        String courseCode,
        String courseName,
        Long courseSemester,
        Double courseCredit,
        Department courseDepartment

) {
}
