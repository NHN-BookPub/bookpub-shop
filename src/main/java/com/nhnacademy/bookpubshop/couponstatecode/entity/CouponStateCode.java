package com.nhnacademy.bookpubshop.couponstatecode.entity;

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
 * 쿠폰 상태 코드(coupon_state_code) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupon_state_code")
public class CouponStateCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_state_code_number", nullable = false)
    private Integer codeNo;

    @Column(name = "coupon_state_code_target", nullable = false, unique = true)
    private String codeTarget;

    @Column(name = "coupon_state_code_used", nullable = false)
    private boolean codeUsed;

    @Column(name = "coupon_state_code_info", nullable = false)
    private String codeInfo;

}
