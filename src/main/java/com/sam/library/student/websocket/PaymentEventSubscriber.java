package com.sam.library.student.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PaymentEventMessage event = objectMapper.readValue(message.getBody(), PaymentEventMessage.class);
            log.info("Payment event received: {}", event.getPaymentNo());
            messagingTemplate.convertAndSend("/topic/payments", event);
        } catch (Exception e) {
            log.error("Failed to process payment event", e);
        }
    }
}
