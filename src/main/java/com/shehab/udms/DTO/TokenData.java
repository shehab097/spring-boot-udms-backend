package com.shehab.udms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenData {
    private Long courseId;
    private Long semesterId;
    private long timestamp;
}