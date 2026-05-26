package com.sam.library.student.service;

import com.sam.library.student.dto.StockAlertDTO;
import com.sam.library.student.event.StockAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Listens for {@link StockAlertEvent}s and broadcasts a
 * {@link StockAlertDTO} payload to all WebSocket subscribers on
 * {@code /topic/stock-alerts}.
 *
 * <p>Clients subscribe to {@code /topic/stock-alerts} to receive real-time
 * low-stock and restocked notifications.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockAlertBroadcastService {

    private static final String TOPIC = "/topic/stock-alerts";

    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @EventListener
    public void onStockAlert(StockAlertEvent event) {
        StockAlertDTO payload = new StockAlertDTO(
                event.getProductId(),
                event.getProductName(),
                event.getCurrentStock(),
                event.getMinStockLevel(),
                event.getAlertType(),
                Instant.now()
        );

        log.info("Stock alert [{}] for product '{}' (id={}) – stock={}, threshold={}",
                event.getAlertType(),
                event.getProductName(),
                event.getProductId(),
                event.getCurrentStock(),
                event.getMinStockLevel());

        messagingTemplate.convertAndSend(TOPIC, payload);
    }
}
