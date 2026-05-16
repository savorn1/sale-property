package com.sam.library.student.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastMessageInfo {
    private Long messageId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;
}
