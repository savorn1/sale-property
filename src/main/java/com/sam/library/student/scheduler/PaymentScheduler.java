package com.sam.library.student.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sam.library.student.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {

    private final PaymentService paymentService;

    // Runs every 5 minutes; marks UNPAID payments older than 10 minutes as PAID
    @Scheduled(cron = "0 */1 * * * *")
    public void autoMarkExpiredPayments() {
        log.info("Running payment expiry check...");
        paymentService.autoMarkExpiredPaymentsAsPaid();
        log.info("Payment expiry check completed.");
    }
}
