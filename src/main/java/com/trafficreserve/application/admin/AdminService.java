package com.trafficreserve.application.admin;

import com.trafficreserve.api.admin.AdminStatsResponse;
import com.trafficreserve.domain.product.ProductRepository;
import com.trafficreserve.domain.reservation.ReservationRepository;
import com.trafficreserve.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    public AdminStatsResponse getStats() {
        long totalSuccess = reservationRepository.countByStatus(ReservationStatus.SUCCESS);
        List<AdminStatsResponse.ProductParams> productStocks = productRepository.findAll().stream()
                .map(p -> new AdminStatsResponse.ProductParams(p.getId(), p.getName(), p.getStock()))
                .collect(Collectors.toList());

        return new AdminStatsResponse(totalSuccess, productStocks);
    }
}
