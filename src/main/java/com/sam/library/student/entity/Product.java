package com.sam.library.student.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;


    @Column(length = 50, nullable = true)
    private String description;

    @Column(nullable = false)
    private Double price = 0.0; 


    // //Getter and Setter for name
    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // // Getter and Setter for description
    // public String getDescription() {
    //     return description;
    // }

    // public void setDescription(String description) {
    //     this.description = description;
    // }

    // // Getter and Setter for price
    // public Double getPrice() {
    //     return price;
    // }

    // public void setPrice(Double price) {
    //     this.price = price;
    // }

}
