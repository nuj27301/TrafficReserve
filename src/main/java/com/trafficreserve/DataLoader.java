package com.trafficreserve;

import com.trafficreserve.domain.member.Member;
import com.trafficreserve.domain.member.MemberRepository;
import com.trafficreserve.domain.product.Product;
import com.trafficreserve.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. 회원 데이터 생성 (없을 때만)
        if (memberRepository.count() == 0) {
            memberRepository.save(new Member("김철수", "kim@example.com"));
            System.out.println("✅ 테스트 회원 데이터 삽입 완료");
        }

        // 2. 현재 사용 가능한 회원 ID 출력 (안전하게 수정)
        List<Member> members = memberRepository.findAll();
        if (!members.isEmpty()) {
            System.out.println("🚀 현재 예약 가능한 회원 ID는 [" + members.get(0).getId() + "] 번입니다!");
        }

        // 3. 상품 데이터 생성 (없을 때만)
        if (productRepository.count() == 0) {
            productRepository.save(new Product("콘서트 티켓 - VIP", 150000L, 100));
            productRepository.save(new Product("콘서트 티켓 - 일반석", 80000L, 200));
            productRepository.save(new Product("한정판 굿즈", 50000L, 50));
            productRepository.save(new Product("팬미팅 입장권", 120000L, 75));
            System.out.println("✅ 테스트 상품 데이터가 삽입되었습니다.");
        }
    }
}