package com.sam.library.student.dto;

import com.sam.library.student.enums.OpenItemStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OpenItemDTO {
    private Long id;
    private String openItemNo;
    private LocalDate itemDate;
    private OpenItemStatus status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OpenItemDetailDTO> details;
}
