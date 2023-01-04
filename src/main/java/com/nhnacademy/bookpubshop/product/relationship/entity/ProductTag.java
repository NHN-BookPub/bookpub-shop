package com.nhnacademy.bookpubshop.product.relationship.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.io.Serializable;
import javax.persistence.Column;
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
import lombok.ToString;

/**
 * 상품태그(product_and_tag) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product_and_tag")
public class ProductTag {
    @EmbeddedId
    private Pk pk;

    @MapsId("tagNo")
    @ManyToOne
    @JoinColumn(name = "tag_number", nullable = false, unique = true)
    private Tag tag;

    @MapsId("productNo")
    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false, unique = true)
    private Product product;

    /**
     * 상품태그(product_and_tag) 테이블 Pk
     * (상품번호, 태그번호).
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
        private String tagNo;
        private Long productNo;
    }
}
