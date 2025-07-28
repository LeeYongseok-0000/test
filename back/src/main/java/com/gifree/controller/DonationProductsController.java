package com.gifree.controller;

import com.gifree.domain.DonationProducts;
import com.gifree.service.DonationProductsService;
import com.gifree.dto.DonationRequestDTO;
import com.gifree.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/donations")
public class DonationProductsController {

    private final DonationProductsService donationProductsService;

    @PostMapping
public ResponseEntity<DonationProducts> createDonation(
    @Valid @RequestBody DonationRequestDTO request
) {
    // 임시로 하드코딩된 이메일 사용 (테스트용)
    request.setDonorEmail("123@aaa.com");
    DonationProducts saved = donationProductsService.saveDonation(request);
    return ResponseEntity.ok(saved);
}

    @GetMapping("/donor/{email}")
    public ResponseEntity<List<DonationProducts>> getDonationsByDonor(@PathVariable String email) {
        List<DonationProducts> donations = donationProductsService.getDonationsByDonorEmail(email);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/{dno}")
    public ResponseEntity<DonationProducts> getDonation(@PathVariable Long dno) {
        return donationProductsService.getDonation(dno)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
