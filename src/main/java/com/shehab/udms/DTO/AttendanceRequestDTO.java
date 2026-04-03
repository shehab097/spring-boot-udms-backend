package com.shehab.udms.DTO;

import lombok.Data;

@Data
public class AttendanceRequestDTO {
    private String qrToken; // QR কোডের ভেতর থাকা এনক্রিপ্টেড ডাটা
    private Long courseId;
    private Long semesterId;
}
