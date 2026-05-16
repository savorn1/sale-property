package com.sam.library.student.websocket;

import lombok.Data;

@Data
public class TypingEvent {
    private Long conversationId;
    private Long userId;
    private boolean typing;
}
