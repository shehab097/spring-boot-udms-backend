package com.shehab.udms.DTO;

import com.shehab.udms.types.Department;


import java.time.LocalDateTime;


public record NoticeDTO(
        Long id,
        String title,
        String content,
        LocalDateTime date,
        String postBy,
        int noticeForSem,
        Department department
        ) {
}
