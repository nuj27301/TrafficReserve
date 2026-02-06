package com.trafficreserve.concurrency;

import com.trafficreserve.application.reservation.ReservationService;
import com.trafficreserve.domain.member.Member;
import com.trafficreserve.domain.member.MemberRepository;
import com.trafficreserve.domain.product.Product;
import com.trafficreserve.domain.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private com.trafficreserve.domain.reservation.ReservationRepository reservationRepository;

    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();

        member = memberRepository.save(new Member("Tester", "tester@test.com"));
        product = productRepository.save(new Product("Limited Item", 10000L, 100));
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("100 concurrent requests should decrement stock to 0")
    void concurrencyTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.reserve(member.getId(), product.getId(), 1);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStock()).isEqualTo(0);
    }
}
