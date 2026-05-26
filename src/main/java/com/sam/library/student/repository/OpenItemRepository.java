package com.sam.library.student.repository;

import com.sam.library.student.entity.OpenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OpenItemRepository extends JpaRepository<OpenItem, Long>, JpaSpecificationExecutor<OpenItem> {
}
