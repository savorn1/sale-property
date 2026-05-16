package com.sam.library.student.dto.chat;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.library.student.entity.chat.Attachment;
import com.sam.library.student.entity.chat.ForwardedFrom;
import com.sam.library.student.entity.chat.Poll;
import com.sam.library.student.enums.MessageType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMessageDTO {

    @NotNull
    private Long conversationId;

    private MessageType type = MessageType.TEXT;
    private String content;
    private List<Attachment> attachments;
    private Long replyTo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;
    private Poll poll;
    private ForwardedFrom forwardedFrom;
    private List<Long> mentions;
}
