package com.nhnacademy.bookpubshop.pricepolicy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 가격정책(price_policy) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "price_policy")
public class PricePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_policy_number", nullable = false)
    private Integer policyNo;

    @Column(name = "price_policy_name", nullable = false)
    private String policyName;

    @Column(name = "price_policy_fee", nullable = false)
    private Long policyFee;
}
