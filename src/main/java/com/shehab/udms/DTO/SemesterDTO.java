package com.shehab.udms.DTO;


import java.time.LocalDateTime;

public record SemesterDTO(
        int id,
        int semesterNo,
        int batch,
        String session,
        String addedBy,
        LocalDateTime addedTime
) {}