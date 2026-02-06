package com.trafficreserve.api.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminStatsResponse {
    private final long totalSuccess;
    private final List<ProductParams> productStocks;

    @Getter
    @RequiredArgsConstructor
    public static class ProductParams {
        private final Long id;
        private final String name;
        private final Integer stock;
    }
}
