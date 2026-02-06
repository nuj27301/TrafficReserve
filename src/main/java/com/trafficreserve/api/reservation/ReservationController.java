package com.trafficreserve.api.reservation;

import com.trafficreserve.api.reservation.dto.ReservationRequest;
import com.trafficreserve.application.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> reserve(@RequestBody ReservationRequest request) {
        reservationService.reserve(request.getMemberId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }
}
