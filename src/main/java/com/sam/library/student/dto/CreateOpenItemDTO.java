package com.sam.library.student.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOpenItemDTO {

    @NotNull
    private LocalDate itemDate;

    private String remark;

    @NotEmpty
    @Valid
    private List<CreateOpenItemDetailDTO> details;
}
