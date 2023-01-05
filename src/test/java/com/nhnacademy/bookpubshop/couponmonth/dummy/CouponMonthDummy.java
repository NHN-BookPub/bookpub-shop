package com.nhnacademy.bookpubshop.couponmonth.dummy;

import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import java.time.LocalDateTime;

/**
 * 이달의쿠폰 더미클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponMonthDummy {

    public static CouponMonth dummy(CouponTemplate couponTemplate) {

        return new CouponMonth(
                null,
                couponTemplate,
                LocalDateTime.now(),
                1
        );
    }
}
