package com.sam.library.student.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOpenItemDetailDTO {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer qty;

    private String remark;
}
