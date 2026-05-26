package com.sam.library.student.mapper;

import com.sam.library.student.dto.OpenItemDTO;
import com.sam.library.student.dto.OpenItemDetailDTO;
import com.sam.library.student.entity.OpenItem;
import com.sam.library.student.entity.OpenItemDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenItemMapper {

    public OpenItemDTO toDTO(OpenItem openItem) {
        OpenItemDTO dto = new OpenItemDTO();
        dto.setId(openItem.getId());
        dto.setOpenItemNo(openItem.getOpenItemNo());
        dto.setItemDate(openItem.getItemDate());
        dto.setStatus(openItem.getStatus());
        dto.setRemark(openItem.getRemark());
        dto.setCreatedAt(openItem.getCreatedAt());
        dto.setUpdatedAt(openItem.getUpdatedAt());

        List<OpenItemDetailDTO> details = openItem.getDetails().stream()
                .map(this::toDetailDTO)
                .collect(Collectors.toList());
        dto.setDetails(details);

        return dto;
    }

    public OpenItemDetailDTO toDetailDTO(OpenItemDetail d) {
        OpenItemDetailDTO dto = new OpenItemDetailDTO();
        dto.setId(d.getId());
        dto.setOpenItemId(d.getOpenItem().getId());
        dto.setProductId(d.getProduct().getId());
        dto.setProductName(d.getProduct().getName());
        dto.setQty(d.getQty());
        dto.setRemark(d.getRemark());
        return dto;
    }
}
