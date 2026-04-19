package com.shehab.udms.model;

import com.shehab.udms.types.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString // for debug
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "course_id", "semester_id", "date"})
        },
        indexes = {
                @Index(name = "idx_student", columnList = "student_id"),
                @Index(name = "idx_course", columnList = "course_id"),
                @Index(name = "idx_date", columnList = "date")
        }
)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ removed optional=false
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    private Semester semester;

    @Column // ✅ nullable by default
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    private LocalDateTime markedAt;

    private String updatedBy;
}