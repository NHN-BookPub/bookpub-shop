package com.nhnacademy.bookpubshop.product.relationship.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품태그(product_and_tag) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_and_tag")
public class ProductTag {
    @EmbeddedId
    private Pk pk;

    @MapsId("tagNo")
    @ManyToOne
    @JoinColumn(name = "tag_number")
    @NotNull
    private Tag tag;

    @MapsId("productNo")
    @ManyToOne
    @JoinColumn(name = "product_number")
    @NotNull
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
        private Integer tagNo;
        private Long productNo;
    }
}
