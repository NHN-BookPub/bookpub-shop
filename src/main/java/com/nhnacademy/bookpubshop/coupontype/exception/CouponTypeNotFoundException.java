package com.nhnacademy.bookpubshop.coupontype.exception;

/**
 * 쿠폰유형이 없을때 발생하는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTypeNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Not Found CouponType : ";

    public CouponTypeNotFoundException(Long typeNo) {
        super(MESSAGE + typeNo);
    }
}
