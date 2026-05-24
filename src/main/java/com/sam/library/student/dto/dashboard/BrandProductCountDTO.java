package com.sam.library.student.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandProductCountDTO {
    private String brandName;
    private Long productCount;
}
