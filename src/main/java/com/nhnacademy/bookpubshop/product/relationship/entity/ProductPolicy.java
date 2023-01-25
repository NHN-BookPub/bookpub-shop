package com.nhnacademy.bookpubshop.product.relationship.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품정책(product_policy) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_policy")
public class ProductPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_policy_number")
    private Integer policyNo;

    @NotNull
    @Column(name = "product_policy_method")
    private String policyMethod;

    @Column(name = "product_policy_saved")
    private boolean policySaved;

    @Column(name = "product_policy_save_rate")
    private Integer saveRate;

    /**
     * 상품 정책을 수정하기 위한 메서드.
     *
     * @param policyMethod 적립 방법
     * @param policySaved  적립 여부
     * @param saveRate     적립률
     */
    public void modifyPolicy(String policyMethod, boolean policySaved,
                             Integer saveRate) {
        this.policyMethod = policyMethod;
        this.policySaved = policySaved;
        this.saveRate = saveRate;
    }
}
