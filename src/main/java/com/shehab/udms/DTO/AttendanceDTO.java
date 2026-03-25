package com.shehab.udms.DTO;

import com.shehab.udms.types.Status;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record AttendanceDTO(
        Long id,

        StudentSimpleDTO student,//
        CourseSimpleDTO course,//
        SemesterSimpleDTO semester,//

        LocalDate date,
        Status status,
        LocalDateTime markedAt,
        String updatedBy
) {
}
