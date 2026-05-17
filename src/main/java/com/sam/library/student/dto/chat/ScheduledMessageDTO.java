package com.sam.library.student.dto.chat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.library.student.enums.MessageType;
import com.sam.library.student.enums.ScheduledMessageStatus;

import lombok.Data;

@Data
public class ScheduledMessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private String content;
    private MessageType type;
    private Long replyTo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledFor;
    private ScheduledMessageStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
