package com.gifree.repository;

import com.gifree.domain.DonationProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationProductsRepository extends JpaRepository<DonationProducts, Long> {
    // 특정 기부자 이메일로 기부내역 조회
    List<DonationProducts> findByDonor_Email(String email);
}