package com.nhnacademy.bookpubshop.wishlist.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
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
 * 위시리스트(wishlist) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "wishlist")
public class Wishlist {
    @EmbeddedId
    private Pk pk;

    // TODO 회원 join
//    @MapsId()
//    @ManyToOne
//    @JoinColumn(name = "member_number")

    @MapsId("productNo")
    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false, unique = true)
    private Product product;

    @Column(name = "wishlist_applied")
    private boolean wishlistApplied;

    /**
     * 위시리스트(wishlist) 테이블 Pk
     * (회원번호, 상품번호).
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
        private Long memberNo;
        private Long productNo;
    }
}