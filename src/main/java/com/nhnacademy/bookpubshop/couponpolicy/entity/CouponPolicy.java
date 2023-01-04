package com.nhnacademy.bookpubshop.couponpolicy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
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
    private Integer policyNumber;

    @Column(name = "coupon_policy_fixed")
    private boolean policyFixed;

    @Column(name = "coupon_policy_discount_rate")
    private Long discountRate;

    @Column(name = "coupon_policy_minimum")
    private Long policyMinimum;

    @Column(name = "coupon_policy_max_discount")
    private Long maxDiscount;

}
