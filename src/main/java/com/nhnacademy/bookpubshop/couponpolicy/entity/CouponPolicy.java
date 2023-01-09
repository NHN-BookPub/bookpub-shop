package com.nhnacademy.bookpubshop.couponpolicy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰정책(coupon_policy) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupon_policy")
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_policy_number")
    private Integer policyNo;

    @Column(name = "coupon_policy_fixed")
    private boolean policyFixed;

    @Column(name = "coupon_policy_discount_rate", nullable = false)
    private Long discountRate;

    @Column(name = "coupon_policy_minimum", nullable = false)
    private Long policyMinimum;

    @Column(name = "coupon_policy_max_discount", nullable = false)
    private Long maxDiscount;

    /**
     * 쿠폰정책 수정을 위한 메소드입니다.
     *
     * @param policyFixed   정액여부
     * @param discountRate  할인가격
     * @param policyMinimum 최소주문금액
     * @param maxDiscount   최대할인가격
     */
    public void modifyCouponPolicy(boolean policyFixed, Long discountRate,
                              Long policyMinimum, Long maxDiscount) {
        this.policyFixed = policyFixed;
        this.discountRate = discountRate;
        this.policyMinimum = policyMinimum;
        this.maxDiscount = maxDiscount;
    }
}
