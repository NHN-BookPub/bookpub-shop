package com.nhnacademy.bookpubshop.couponstatecode.exception;

/**
 * 쿠폰상태코드 Not Found 예외.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponStateCodeNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Not Found CouponStateCode: ";

    public CouponStateCodeNotFoundException(Integer codeNo) {
        super(MESSAGE + codeNo);
    }
}
