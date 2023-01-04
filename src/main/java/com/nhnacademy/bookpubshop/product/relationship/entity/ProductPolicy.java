package com.nhnacademy.bookpubshop.product.relationship.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product_relation")
public class ProductPolicy {
    @Id
    @Column(name = "product_policy_number")
    private Integer policyNo;

    @Column(name = "product_policy_method")
    private String policyMethod;

    @Column(name = "product_policy_saved")
    private boolean policySaved;

    @Column(name = "product_policy_save_rate")
    private Integer saveRate;
}
