package com.gifree.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_donation_product")
@Getter
@ToString(exclude = "donor")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonationProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;

    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "pno", nullable = false)
private Product product;  // 필수!

    /** 기부자 (Member 엔티티) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private Member donor;

    /** 기부 금액 */
    @Column(nullable = false)
    private Integer amount;

    /** 기부 횟수 */
    @Column(nullable = false)
    private Integer count;
}
