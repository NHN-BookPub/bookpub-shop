package com.nhnacademy.bookpubshop.tier.relationship.entity;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * 등급과 쿠폰에대한 관계 테이블입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Entity
@Table(name = "tier_and_coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TierCoupon {
    @EmbeddedId
    private Pk pk;

    @MapsId(value = "couponTemplateNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_template_number")
    @NotNull
    private CouponTemplate couponTemplate;

    @MapsId(value = "tierNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_number")
    @NotNull
    private BookPubTier bookPubTier;

    /**
     * 등급과 쿠폰관계에대한 pk 입니다.
     */
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Getter
    public static class Pk implements Serializable {
        private Long couponTemplateNo;
        private Integer tierNo;
    }

}
