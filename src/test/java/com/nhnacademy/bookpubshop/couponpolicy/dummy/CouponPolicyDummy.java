package com.nhnacademy.bookpubshop.couponpolicy.dummy;

import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;

/**
 * 쿠폰정책 더미클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponPolicyDummy {

    public static CouponPolicy dummy() {

        return new CouponPolicy(
                null,
                true,
                1000L,
                1000L,
                1000L
        );
    }
}
