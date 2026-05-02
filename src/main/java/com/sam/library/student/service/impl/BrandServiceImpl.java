package com.sam.library.student.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sam.library.student.entity.Brand;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.BrandRepository;
import com.sam.library.student.service.BrandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands(){
        return brandRepository.findAll();
    }

    @Override
    public Page<Brand> getAllBrands(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand getBrandById(Long id){
        return brandRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Brand", id));
    }

    @Override
    public Brand createBrand(Brand brand){
        String name = brand.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty.");
        }

        if (brandRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Brand with name '" + name + "' already exists.");
        }

        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(Long id, Brand brand){
       Brand existingBrand = brandRepository.findById(id).orElse(null);

       if (existingBrand == null) {
           throw new ResourceNotFoundException("Brand", id);
       }

        String name = brand.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty.");
        }
        existingBrand.setName(name);

        return brandRepository.save(existingBrand);
    }


    @Override
    public String deleteBrand(Long id){
        Brand existingBrand = brandRepository.findById(id).orElse(null);
        if (existingBrand == null) {
            throw new ResourceNotFoundException("Brand", id);
        }
        brandRepository.delete(existingBrand);
        return "Brand with id " + id + " has been deleted successfully.";
    }

    @Override
    public String deleteBrands(List<Long> ids) {
        List<Brand> brandsToDelete = brandRepository.findAllById(ids);
        if (brandsToDelete.isEmpty()) {
            return "No brands found for the provided IDs.";
        }
        brandRepository.deleteAll(brandsToDelete);

        return "Deleted " + brandsToDelete.size() + " brands successfully.";
    }
}