package com.sam.library.student.dto.chat;

import com.sam.library.student.enums.MessageType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateScheduledMessageDTO {

    @NotNull
    private Long conversationId;

    private String content;
    private MessageType type = MessageType.TEXT;
    private Long replyTo;

    @NotNull
    @Future
    private LocalDateTime scheduledFor;
}
