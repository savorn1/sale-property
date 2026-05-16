package com.sam.library.student.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserConversationDTO {
    private Long id;
    private Long userId;
    private Long conversationId;
    private Long lastReadMessageId;
    private int unreadCount;
    private LocalDateTime joinedAt;
    private boolean muted;
    private List<Long> starredMessageIds;
    private boolean archived;
}
