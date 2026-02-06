package com.trafficreserve.application.reservation;

import com.trafficreserve.domain.member.Member;
import com.trafficreserve.domain.member.MemberRepository;
import com.trafficreserve.domain.product.Product;
import com.trafficreserve.domain.product.ProductRepository;
import com.trafficreserve.domain.reservation.Reservation;
import com.trafficreserve.domain.reservation.ReservationRepository;
import com.trafficreserve.global.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    @DistributedLock(key = "#productId")
    public void reserve(Long memberId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        product.decreaseStock(quantity);

        Reservation reservation = Reservation.createReservation(member, product, quantity);
        reservation.confirmReservation(); // Immediate success for simplicity in this step

        Reservation saved = reservationRepository.save(reservation);

        eventPublisher.publishEvent(new com.trafficreserve.domain.reservation.event.ReservationSuccessEvent(
                saved.getId(), productId, memberId));
    }
}
