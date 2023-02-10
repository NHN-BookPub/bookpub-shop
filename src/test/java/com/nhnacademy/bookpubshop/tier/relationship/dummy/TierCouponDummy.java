package com.nhnacademy.bookpubshop.tier.relationship.dummy;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon.Pk;

/**
 * 등급 쿠폰 더미클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class TierCouponDummy {

    public static TierCoupon dummy(CouponTemplate couponTemplate, BookPubTier tier) {
        return new TierCoupon(
                new Pk(1L, 1),
                couponTemplate,
                tier
        );
    }

}
