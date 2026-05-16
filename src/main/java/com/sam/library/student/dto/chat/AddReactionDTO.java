package com.sam.library.student.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddReactionDTO {

    @NotBlank
    private String emoji;
}
