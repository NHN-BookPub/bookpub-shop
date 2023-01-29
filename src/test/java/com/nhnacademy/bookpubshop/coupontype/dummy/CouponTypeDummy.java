package com.nhnacademy.bookpubshop.coupontype.dummy;

import static com.nhnacademy.bookpubshop.state.CouponType.COMMON;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;

/**
 * 쿠폰유형 더미클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTypeDummy {

    public static CouponType dummy() {

        return new CouponType(
                null,
                COMMON.getName()
        );
    }
}
