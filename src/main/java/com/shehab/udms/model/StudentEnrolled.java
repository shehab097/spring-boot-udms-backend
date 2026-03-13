package com.shehab.udms.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Course enrolled by student
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "student_enrolled", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "semester_id"})
})
public class StudentEnrolled {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // আইডি অটো জেনারেট হবে
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToMany
    @JoinTable(
            name = "student_enrollment_courses",
            joinColumns = @JoinColumn(name = "enrolled_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses;
}
