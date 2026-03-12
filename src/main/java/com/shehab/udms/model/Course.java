package com.shehab.udms.model;

import com.shehab.udms.types.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private int courseSemester;

    @Column(nullable = false)
    private double courseCredit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department courseDepartment;

//    @Column(nullable = false)
//    private String addedBy;
//
//    @Column(nullable = false)
//    private LocalDateTime addingTime;
}

/*
### Courses

- c_id (PK)
- c_code
- c_name
- c_credit
- c_sem_id (FK)
- c_teacher_id (FK)
- c_dept_id (FK)
 */
