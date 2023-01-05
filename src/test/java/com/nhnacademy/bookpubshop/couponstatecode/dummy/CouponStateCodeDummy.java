package com.nhnacademy.bookpubshop.couponstatecode.dummy;

import static com.nhnacademy.bookpubshop.state.CouponState.COUPON_ALL;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;

/**
 * 쿠폰상태코드 더미클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponStateCodeDummy {

    public static CouponStateCode dummy() {

        return new CouponStateCode(
                null,
                COUPON_ALL.getName(),
                COUPON_ALL.isUsed(),
                "test_codeInfo"
        );
    }
}
