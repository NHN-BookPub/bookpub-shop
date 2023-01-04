package com.nhnacademy.bookpubshop.coupontype.entity;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "coupon_type")
public class CouponType {

    @Id
    @Column(name = "coupon_type_number")
    private Long typeNo;

    @Column(name = "coupon_type_name")
    private String typeName;



}
