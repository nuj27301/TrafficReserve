package com.trafficreserve.application.reservation.event;

import com.trafficreserve.domain.reservation.event.ReservationSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @EventListener
    public void handleReservationSuccess(ReservationSuccessEvent event) {
        log.info("Sending reservation success notification for reservationId: {}", event.getReservationId());
        String message = String.format("Member %d succeeded to reserve Product %d", event.getMemberId(),
                event.getProductId());
        messagingTemplate.convertAndSend("/topic/logs", message);
    }
}
