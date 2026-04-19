package com.shehab.udms.DTO;

import com.shehab.udms.model.Course;
import com.shehab.udms.types.Status;

public record AttendanceTinyDto(

        String courseName,
        String courseCode,
        Status status,
        SemesterSimpleDTO currSemester
) {

}
