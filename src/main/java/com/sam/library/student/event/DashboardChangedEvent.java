package com.sam.library.student.event;

import org.springframework.context.ApplicationEvent;

/**
 * Published whenever order or payment data changes so that the
 * DashboardBroadcastService can push a fresh snapshot to all
 * WebSocket subscribers on /topic/dashboard.
 */
public class DashboardChangedEvent extends ApplicationEvent {

    public DashboardChangedEvent(Object source) {
        super(source);
    }
}
