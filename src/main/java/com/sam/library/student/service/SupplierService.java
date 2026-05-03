package com.sam.library.student.service;

import com.sam.library.student.dto.SupplierFilterRequest;
import com.sam.library.student.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {
    Page<Supplier> getAllSuppliers(SupplierFilterRequest filter, Pageable pageable);
    Supplier getSupplierById(Long id);
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Long id, Supplier supplier);
    String deleteSupplier(Long id);
    String deleteSuppliers(List<Long> ids);
}
