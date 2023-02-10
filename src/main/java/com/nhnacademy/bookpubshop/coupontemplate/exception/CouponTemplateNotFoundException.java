package com.nhnacademy.bookpubshop.coupontemplate.exception;

/**
 * 쿠폰템플릿 번호를 찾을 수 없을 때 발생하는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTemplateNotFoundException extends RuntimeException {
    public static final String MESSAGE = "은 없는 쿠폰템플릿번호입니다.";

    public CouponTemplateNotFoundException(Long templateNo) {
        super(templateNo + MESSAGE);
    }

    public CouponTemplateNotFoundException() {}
}
