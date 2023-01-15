package com.nhnacademy.bookpubshop.coupon.exception;

/**
 * 쿠폰 번호가 없을시 생기는 에러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponNotFoundException extends RuntimeException {
    public static final String MESSAGE = "은 없는 쿠폰번호입니다.";

    public CouponNotFoundException(Long couponNo) {
        super(couponNo + MESSAGE);
    }
}
