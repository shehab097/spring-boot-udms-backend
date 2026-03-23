package com.shehab.udms.model;

import com.shehab.udms.types.Department;
import com.shehab.udms.types.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String studentID;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer currSemester;   // for integrating attendance system

    @Enumerated(EnumType.STRING)
    private Department department;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "student",fetch = FetchType.LAZY)
    private List<Attendance> attendances;

}
