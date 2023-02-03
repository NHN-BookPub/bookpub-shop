package com.nhnacademy.bookpubshop.coupon.exception;

/**
 * 쿠폰을 찾지 못했을 때 발생하는 exception.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundCouponException extends RuntimeException {

    public static final String MESSAGE = "존재하지 않는 쿠폰입니다 (쿠폰번호 : ";

    public NotFoundCouponException(Long couponNo) {
        super(MESSAGE + couponNo + ")");
    }
}
