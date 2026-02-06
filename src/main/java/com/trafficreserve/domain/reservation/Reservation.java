package com.trafficreserve.domain.reservation;

import com.trafficreserve.domain.member.Member;
import com.trafficreserve.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Integer quantity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 연관관계 편의 메서드
    public static Reservation createReservation(Member member, Product product, Integer quantity) {
        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setProduct(product);
        reservation.quantity = quantity;
        reservation.status = ReservationStatus.PENDING;
        return reservation;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setProduct(Product product) {
        this.product = product;
    }

    // 상태 변경 메서드
    public void confirmReservation() {
        this.status = ReservationStatus.SUCCESS;
    }

    public void failReservation() {
        this.status = ReservationStatus.FAILED;
    }
}
