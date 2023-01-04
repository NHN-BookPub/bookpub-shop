package com.nhnacademy.bookpubshop.product.relationship.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 상품카테고리관계(product_and_category) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product_and_category")
public class ProductCategory {
    @EmbeddedId
    private Pk pk;

    /**
     * 상품카테고리관계(product_and_category) 테이블 Pk
     * (카테고리번호, 상품번호).
     *
     * @author : 박경서
     * @since : 1.0
     **/
    @EqualsAndHashCode
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "category_number")
        private Integer categoryNo;

        @Column(name = "product_number")
        private Long productNo;
    }
}
