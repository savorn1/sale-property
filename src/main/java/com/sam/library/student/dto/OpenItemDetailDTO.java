package com.sam.library.student.dto;

import lombok.Data;

@Data
public class OpenItemDetailDTO {
    private Long id;
    private Long openItemId;
    private Long productId;
    private String productName;
    private Integer qty;
    private String remark;
}
