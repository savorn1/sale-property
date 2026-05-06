package com.sam.library.student.repository;

import com.sam.library.student.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
}
