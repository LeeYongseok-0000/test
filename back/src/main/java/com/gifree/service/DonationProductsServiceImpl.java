package com.gifree.service;

import com.gifree.domain.DonationProducts;
import com.gifree.domain.Member;
import com.gifree.domain.Product;
import com.gifree.dto.DonationRequestDTO;
import com.gifree.repository.DonationProductsRepository;
import com.gifree.repository.MemberRepository;
import com.gifree.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor  // final 필드 생성자 자동 생성
public class DonationProductsServiceImpl implements DonationProductsService {

    private final DonationProductsRepository donationProductsRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional 
    public DonationProducts saveDonation(DonationRequestDTO request) {

        // 기부자 정보 조회
        Member donor = memberRepository.findById(request.getDonorEmail())
                .orElseThrow(() -> new IllegalArgumentException("기부자 회원이 존재하지 않습니다."));

        // 1. DTO의 정보로 새로운 Product 엔티티를 생성합니다.
        Product newProduct = Product.builder()
                .pname(request.getPname())
                .brand(request.getBrand())
                .price(request.getAmount()) // 기부 금액을 상품 가격으로 설정 (혹은 다른 비즈니스 로직 적용)
                // ... Product 엔티티에 필요한 다른 필드가 있다면 설정
                .build();
        
        // 2. 새로운 Product를 DB에 먼저 저장합니다. (그래야 pno가 생성됨)
        Product savedProduct = productRepository.save(newProduct);

        // 3. 저장된 Product를 사용하여 DonationProducts 엔티티를 생성합니다.
        DonationProducts donation = DonationProducts.builder()
                .product(savedProduct) // ⭐️ 방금 저장한 Product 객체를 사용
                .donor(donor)
                .amount(request.getAmount())
                .count(request.getCount())
                .build();

        // 4. 최종적으로 DonationProducts를 저장합니다.
        return donationProductsRepository.save(donation);
    }

    @Override
    public List<DonationProducts> getDonationsByDonorEmail(String email) {
        return donationProductsRepository.findByDonor_Email(email);
    }

    @Override
    public Optional<DonationProducts> getDonation(Long dno) {
        return donationProductsRepository.findById(dno);
    }
}
