package com.nhnacademy.bookpubshop.couponmonth.exception;

/**
 * 이달의쿠폰 번호가 없을시 생기는 에러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponMonthNotFoundException extends RuntimeException {
    public static final String MESSAGE = "은 없는 이달의 쿠폰번호입니다.";

    public CouponMonthNotFoundException(Long monthNo) {
        super(monthNo + MESSAGE);
    }
}
