package com.nhnacademy.bookpubshop.coupontype.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon_type")
public class CouponType {

    @Id
    private Long typeNumber;

    @Column(name = "coupon_type_name")
    private String typeName;



}
