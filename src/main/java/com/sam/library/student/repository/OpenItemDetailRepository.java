package com.sam.library.student.repository;

import com.sam.library.student.entity.OpenItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenItemDetailRepository extends JpaRepository<OpenItemDetail, Long> {
    List<OpenItemDetail> findByOpenItemId(Long openItemId);
}
