package com.sam.library.student.repository;

import com.sam.library.student.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByName(String name);

    /** Returns [brandName (String), productCount (Long)] rows */
    @Query("SELECT p.brand.name, COUNT(p) FROM Product p WHERE p.brand IS NOT NULL GROUP BY p.brand.id, p.brand.name ORDER BY COUNT(p) DESC")
    List<Object[]> countProductsByBrand();

    /** Returns [categoryName (String), productCount (Long)] rows */
    @Query("SELECT p.category.name, COUNT(p) FROM Product p WHERE p.category IS NOT NULL GROUP BY p.category.id, p.category.name ORDER BY COUNT(p) DESC")
    List<Object[]> countProductsByCategory();
}
