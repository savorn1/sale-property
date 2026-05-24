package com.sam.library.student.service;

import com.sam.library.student.dto.dashboard.DashboardDTO;
import com.sam.library.student.event.DashboardChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Listens for {@link DashboardChangedEvent}s (fired by OrderService and
 * PaymentService after any write operation) and broadcasts the latest
 * dashboard snapshot to all WebSocket subscribers.
 *
 * <p>Topic: {@code /topic/dashboard}</p>
 *
 * <p>A scheduled heartbeat also runs every 30 seconds so newly connected
 * clients always receive fresh data without waiting for the next mutation.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardBroadcastService {

    private static final String TOPIC = "/topic/dashboard";

    private final SimpMessagingTemplate messagingTemplate;
    private final DashboardService dashboardService;

    /**
     * Triggered synchronously inside the transaction-commit listener.
     * The {@code @Async} annotation offloads the DB query + broadcast to a
     * worker thread so we don't slow down the originating HTTP request.
     */
    @Async
    @EventListener
    public void onDashboardChanged(DashboardChangedEvent event) {
        log.debug("DashboardChangedEvent received from '{}', broadcasting …", event.getSource().getClass().getSimpleName());
        push();
    }

    /**
     * Heartbeat: push the latest dashboard snapshot every 30 seconds.
     * This keeps newly connected clients up to date even if no mutations
     * have occurred recently.
     */
    @Scheduled(fixedDelay = 30_000)
    public void scheduledPush() {
        push();
    }

    // ── internal ─────────────────────────────────────────────────────────────

    private void push() {
        try {
            DashboardDTO data = dashboardService.getDashboard();
            if (data == null) {
                log.warn("getDashboard() returned null – skipping broadcast");
                return;
            }
            messagingTemplate.convertAndSend(TOPIC, data);
            log.debug("Dashboard snapshot pushed to {}", TOPIC);
        } catch (Exception e) {
            log.error("Failed to push dashboard snapshot", e);
        }
    }
}
