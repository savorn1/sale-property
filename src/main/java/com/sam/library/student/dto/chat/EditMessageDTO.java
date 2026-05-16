package com.sam.library.student.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditMessageDTO {

    @NotBlank
    private String content;
}
