package com.nhnacademy.bookpubshop.coupontype.entity;

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
 * 쿠폰유형(coupon_type) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "coupon_type")
public class CouponType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_type_number", nullable = false)
    private Long typeNo;

    @Column(name = "coupon_type_name", nullable = false, unique = true)
    private String typeName;

}
