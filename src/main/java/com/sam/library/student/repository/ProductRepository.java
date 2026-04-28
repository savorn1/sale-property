package com.sam.library.student.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sam.library.student.entity.Product;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {
   // ✅ Spring will auto-generate query
   Optional<Product> findById(Long id);
   Optional<Product> findByName(String name);



   // ✅ JPQL instead of native SQL
   @Modifying
   @Transactional
   @Query("UPDATE Product p SET p.price = :price WHERE p.id = :id")
   int updatePrice(@Param("id") Long id, @Param("price") Double price);

   // ✅ Cleaner JPQL update
   @Modifying
   @Transactional
   @Query("""
       UPDATE Product p 
       SET p.name = :name,
           p.description = :description,
           p.price = :price
       WHERE p.id = :id
   """)
   int updateProductDetails(
           @Param("id") Long id,
           @Param("name") String name,
           @Param("description") String description,
           @Param("price") Double price
   );

}
