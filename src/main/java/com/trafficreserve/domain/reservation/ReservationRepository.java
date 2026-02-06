package com.trafficreserve.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countByStatus(ReservationStatus status);
}
