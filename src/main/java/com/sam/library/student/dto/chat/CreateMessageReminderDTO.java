package com.sam.library.student.dto.chat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMessageReminderDTO {

    private Long messageId;
    private Long conversationId;

    @NotNull
    @Future
    private LocalDateTime remindAt;

    private String note;
}
