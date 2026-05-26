package com.sam.library.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReceivePurchaseOrderDTO {
    private List<ReceiveItemDTO> items;

    @Data
    public static class ReceiveItemDTO {
        private Long detailId;
        private Integer receivedQty;
    }
}
