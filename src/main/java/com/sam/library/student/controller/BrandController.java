package  com.sam.library.student.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.library.student.dto.BrandDTO;
import com.sam.library.student.mapper.BrandMapper;
import com.sam.library.student.service.BrandService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/brand")
@Tag(name = "Brand", description = "Brand services APIs")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @GetMapping()
    public ResponseEntity<List<BrandDTO>> getAllBrands(){
         List<BrandDTO> brands = brandService.getAllBrands()
            .stream()
            .map(brandMapper::toBrandDTO)
            .toList();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id){
        return ResponseEntity.ok(brandMapper.toBrandDTO(brandService.getBrandById(id)));
    }

    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO dto){
        return ResponseEntity.ok(brandMapper.toBrandDTO(brandService.createBrand(brandMapper.toBrand(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id){
        return ResponseEntity.ok(brandService.deleteBrand(id));
    }

    
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteBrands(@RequestBody List<Long> ids){
        return ResponseEntity.ok(brandService.deleteBrands(ids));
    }

    


}

