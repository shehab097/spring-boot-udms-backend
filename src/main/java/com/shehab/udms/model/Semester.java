package com.shehab.udms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long semesterNo;

    @Column(nullable = false)
    private Long batch;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private String addedBy;

    @Column(nullable = false)
    private LocalDateTime addedTime;
}
