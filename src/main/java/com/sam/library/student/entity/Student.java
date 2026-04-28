package com.sam.library.student.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String studentId;

    private String password;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
