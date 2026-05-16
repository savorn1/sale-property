package com.sam.library.student.websocket;

import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.chat.SendMessageDTO;
import com.sam.library.student.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Client sends to: /app/chat.send
     * Broadcast to:    /topic/conversations/{conversationId}
     * Message is broadcast inside MessageServiceImpl via SimpMessagingTemplate.
     */
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageDTO dto, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        if (attrs != null) {
            Long userId = (Long) attrs.get("userId");
            if (userId != null) {
                UserContext.set(userId, null);
            }
        }
        try {
            messageService.sendMessage(dto);
        } finally {
            UserContext.clear();
        }
    }

    /**
     * Client sends to: /app/chat.typing
     * Broadcast to:    /topic/conversations/{conversationId}/typing
     */
    @MessageMapping("/chat.typing")
    public void typing(@Payload TypingEvent event) {
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + event.getConversationId() + "/typing", event);
    }
}
