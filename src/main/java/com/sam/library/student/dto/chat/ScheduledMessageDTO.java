package com.sam.library.student.dto.chat;

import com.sam.library.student.enums.MessageType;
import com.sam.library.student.enums.ScheduledMessageStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledMessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private String content;
    private MessageType type;
    private Long replyTo;
    private LocalDateTime scheduledFor;
    private ScheduledMessageStatus status;
    private LocalDateTime createdAt;
}
