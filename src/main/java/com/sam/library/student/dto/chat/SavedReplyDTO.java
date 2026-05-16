package com.sam.library.student.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavedReplyDTO {
    private Long id;
    private Long userId;
    private String title;
    private String shortcut;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
