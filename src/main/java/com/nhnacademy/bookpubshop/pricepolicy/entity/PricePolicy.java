package com.nhnacademy.bookpubshop.pricepolicy.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "price_policy")
public class PricePolicy {

    @Id
    @Column(name = "price_policy_number")
    private Integer policyNo;

    @Column(name = "price_policy_name")
    private String policyName;

    @Column(name = "price_policy_fee")
    private Long policyFee;
}
