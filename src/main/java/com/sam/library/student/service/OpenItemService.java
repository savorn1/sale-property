package com.sam.library.student.service;

import com.sam.library.student.dto.CreateOpenItemDTO;
import com.sam.library.student.entity.OpenItem;
import com.sam.library.student.enums.OpenItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OpenItemService {
    Page<OpenItem> getAll(String q, List<OpenItemStatus> status, Pageable pageable);
    OpenItem getById(Long id);
    OpenItem create(CreateOpenItemDTO dto);

    /** Confirm: locks the document and creates INITIAL_STOCK movements for every detail. */
    OpenItem confirm(Long id);

    /** Cancel a DRAFT (no stock movement). Cancelling a CONFIRMED order reverses stock. */
    OpenItem cancel(Long id);

    /** Delete only allowed on DRAFT documents. */
    void delete(Long id);
}
