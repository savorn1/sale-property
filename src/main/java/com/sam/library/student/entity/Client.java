package com.sam.library.student.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends BaseEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(length = 50, nullable = false, unique = true)
     private String name;


     @Column(length = 100, nullable = false)
     private String email;

     @Column(length = 20, nullable = false)
     private String phone;


     @Column(length = 50, nullable = false)
     private String gender;


     @Column(length = 100, nullable = false)
     private String address;

}
