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
 * 상품유형(product_type_state_code) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_type_state_code")
public class ProductTypeStateCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_state_code_number")
    private Integer codeNo;

    @NotNull
    @Column(name = "product_type_state_code_name", unique = true)
    private String codeName;

    @NotNull
    @Column(name = "product_type_state_code_used")
    private boolean codeUsed;

    @Column(name = "product_type_state_code_info")
    private String codeInfo;
}
