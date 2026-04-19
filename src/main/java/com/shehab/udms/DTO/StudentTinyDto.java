package com.shehab.udms.DTO;

import java.util.List;

public record StudentTinyDto(
        String name,
        String UserId,
        List<AttendanceTinyDto> attendance,
        Long totalClass
//        Long totalPresent,
//        Double attendancePercentage
) {
}
