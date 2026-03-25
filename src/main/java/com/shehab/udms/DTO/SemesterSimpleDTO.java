package com.shehab.udms.DTO;

import java.time.LocalDateTime;

public record SemesterSimpleDTO(
        Long id,
        Long semesterNo,
        Long batch,
        String session
) {
}
