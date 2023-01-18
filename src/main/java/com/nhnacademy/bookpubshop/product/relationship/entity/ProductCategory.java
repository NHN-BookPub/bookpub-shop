package com.nhnacademy.bookpubshop.product.relationship.entity;

import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품카테고리관계(product_and_category) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_and_category")
public class ProductCategory {
    @EmbeddedId
    private Pk pk;

    @MapsId("categoryNo")
    @ManyToOne
    @JoinColumn(name = "category_number")
    private Category category;

    @MapsId("productNo")
    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

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
        private Integer categoryNo;
        private Long productNo;
    }
}
