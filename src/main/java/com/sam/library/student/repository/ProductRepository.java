package com.sam.library.student.repository;

import com.sam.library.student.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByName(String name);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
            UPDATE Product p
            SET p.name = :name,
                p.description = :description,
                p.price = :price,
                p.brand = :brand,
                p.imageUrl = :imageUrl
            WHERE p.id = :id
            """)
    int updateProductDetails(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("price") Double price,
            @Param("brand") com.sam.library.student.entity.Brand brand,
            @Param("imageUrl") String imageUrl
    );
}
