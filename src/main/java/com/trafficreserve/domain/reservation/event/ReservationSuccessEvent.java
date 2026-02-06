package com.trafficreserve.domain.reservation.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationSuccessEvent {
    private final Long reservationId;
    private final Long productId;
    private final Long memberId;
}
