package com.sam.library.student.dto.chat;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserConversationDTO {
    private Long id;
    private Long userId;
    private Long conversationId;
    private Long lastReadMessageId;
    private int unreadCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;
    private boolean muted;
    private List<Long> starredMessageIds;
    private boolean archived;
}
