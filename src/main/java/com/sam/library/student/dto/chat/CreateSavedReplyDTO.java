package com.sam.library.student.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSavedReplyDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String shortcut;

    @NotBlank
    private String content;
}
