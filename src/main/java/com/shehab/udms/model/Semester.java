package com.shehab.udms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int semesterNo;

    @Column(nullable = false)
    private int batch;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private String addedBy;

    @Column(nullable = false)
    private LocalDateTime addedTime;
}
