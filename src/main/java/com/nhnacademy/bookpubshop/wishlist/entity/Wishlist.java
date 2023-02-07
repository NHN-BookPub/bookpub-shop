package com.nhnacademy.bookpubshop.wishlist.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
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

/**
 * 위시리스트(wishlist) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/

@Entity
@Table(name = "wishlist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Wishlist {

    @EmbeddedId
    private Pk pk;

    @MapsId("memberNo")
    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @MapsId("productNo")
    @ManyToOne
    @JoinColumn(name = "product_number")
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
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {
        private Long memberNo;
        private Long productNo;
    }

    /**
     * 알람 여부를 수정하는 메서드.
     */
    public void modifyWishlistAlarm() {
        this.wishlistApplied = !this.wishlistApplied;
    }
}