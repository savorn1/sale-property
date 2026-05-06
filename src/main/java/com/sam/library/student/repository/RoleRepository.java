package com.sam.library.student.repository;

import com.sam.library.student.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
    boolean existsByCode(String code);

    @EntityGraph(attributePaths = "permissions")
    Optional<Role> findWithPermissionsById(Long id);
}
