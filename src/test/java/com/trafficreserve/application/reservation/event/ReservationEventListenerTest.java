package com.trafficreserve.application.reservation.event;

import com.trafficreserve.domain.reservation.event.ReservationSuccessEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationEventListenerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ReservationEventListener reservationEventListener;

    @Test
    @DisplayName("Should send WebSocket notification on reservation success event")
    void handleReservationSuccess() {
        // Given
        ReservationSuccessEvent event = new ReservationSuccessEvent(1L, 100L, 1L);

        // When
        reservationEventListener.handleReservationSuccess(event);

        // Then
        verify(messagingTemplate).convertAndSend(eq("/topic/reservations"), anyString());
    }
}
