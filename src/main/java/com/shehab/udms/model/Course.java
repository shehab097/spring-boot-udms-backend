package com.shehab.udms.model;

import com.shehab.udms.types.Department;
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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private Long courseSemester;

    @Column(nullable = false)
    private double courseCredit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department courseDepartment;

    // adding link with teacher, a course should be associated with a teacher
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // attendence
    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;


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
