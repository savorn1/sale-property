package com.sam.library.student.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "permission")
@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 50)
    private String module;
}
