package com.nhnacademy.bookpubshop.product.relationship.entity;

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
import lombok.ToString;

/**
 * 상품정책(product_policy) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product_policy")
public class ProductPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_policy_number", nullable = false)
    private Integer policyNo;

    @Column(name = "product_policy_method", nullable = false)
    private String policyMethod;

    @Column(name = "product_policy_saved", nullable = false)
    private boolean policySaved;

    @Column(name = "product_policy_save_rate")
    private Integer saveRate;
}
