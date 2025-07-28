package com.gifree.service;

import com.gifree.domain.DonationProducts;
import com.gifree.dto.DonationRequestDTO;

import java.util.List;
import java.util.Optional;

public interface DonationProductsService {

    DonationProducts saveDonation(DonationRequestDTO donationRequestDTO);

    List<DonationProducts> getDonationsByDonorEmail(String email);

    Optional<DonationProducts> getDonation(Long dno);
}