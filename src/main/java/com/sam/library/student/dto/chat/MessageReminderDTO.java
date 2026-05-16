package com.sam.library.student.dto.chat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.library.student.enums.ReminderStatus;

import lombok.Data;

@Data
public class MessageReminderDTO {
    private Long id;
    private Long userId;
    private Long messageId;
    private Long conversationId;
    private LocalDateTime remindAt;
    private String note;
    private String messageContent;
    private ReminderStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
