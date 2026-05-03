package com.sam.library.student.service.impl;

import com.sam.library.student.dto.SupplierFilterRequest;
import com.sam.library.student.entity.Supplier;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.SupplierRepository;
import com.sam.library.student.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Page<Supplier> getAllSuppliers(SupplierFilterRequest filter, Pageable pageable) {
        Specification<Supplier> spec = Specification.where(null);

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getStartDate() != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate().atStartOfDay()));
        }

        if (filter.getEndDate() != null) {
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate().atTime(23, 59, 59)));
        }

        return supplierRepository.findAll(spec, pageable);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        String name = supplier.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be null or empty.");
        }
        if (supplierRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Supplier with name '" + name + "' already exists.");
        }
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
        String name = supplier.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be null or empty.");
        }
        existing.setName(name);
        existing.setPhone(supplier.getPhone());
        existing.setEmail(supplier.getEmail());
        existing.setAddress(supplier.getAddress());
        return supplierRepository.save(existing);
    }

    @Override
    public String deleteSupplier(Long id) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
        supplierRepository.delete(existing);
        return "Supplier with id " + id + " has been deleted successfully.";
    }

    @Override
    public String deleteSuppliers(List<Long> ids) {
        List<Supplier> toDelete = supplierRepository.findAllById(ids);
        if (toDelete.isEmpty()) {
            return "No suppliers found for the provided IDs.";
        }
        supplierRepository.deleteAll(toDelete);
        return "Deleted " + toDelete.size() + " suppliers successfully.";
    }
}
