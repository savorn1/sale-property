package com.sam.library.student.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProductCountDTO {
    private String categoryName;
    private Long productCount;
}
