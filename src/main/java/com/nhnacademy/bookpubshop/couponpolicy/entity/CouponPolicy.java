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
    @Column(name = "coupon_policy_number", nullable = false)
    private Integer policyNo;

    @Column(name = "coupon_policy_fixed", nullable = false)
    private boolean policyFixed;

    @Column(name = "coupon_policy_discount_rate", nullable = false)
    private Long discountRate;

    @Column(name = "coupon_policy_minimum", nullable = false)
    private Long policyMinimum;

    @Column(name = "coupon_policy_max_discount", nullable = false)
    private Long maxDiscount;

}
