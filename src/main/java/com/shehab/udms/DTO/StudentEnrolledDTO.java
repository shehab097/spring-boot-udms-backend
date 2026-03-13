package com.shehab.udms.DTO;


import java.util.List;

/**
 * Record class for Student Enrollment Request.
 * Records are immutable and perfect for DTOs.
 */
public record StudentEnrolledDTO(

        String studentId,
        String name,

        int sem_id,
        int semesterNo,
        int batch,
        String session,

        List<String> courseCodes,
        List<String> courseName
) {}
