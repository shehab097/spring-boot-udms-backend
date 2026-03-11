package com.shehab.udms.model;

import java.time.LocalDateTime;
import java.util.Date;
import com.shehab.udms.types.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String postBy;

    private int noticeForSem;

    @Enumerated(EnumType.STRING)
    private Department department;
}
