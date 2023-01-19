package com.nhnacademy.bookpubshop.couponpolicy.exception;

/**
 * 쿠폰정책이 존재하지 않을 시 발생하는 에러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponPolicyNotFoundException extends RuntimeException {
    public static final String MESSAGE = "번은 없는 쿠폰정책번호입니다.";

    public CouponPolicyNotFoundException(Integer policyNo) {
        super(policyNo + MESSAGE);
    }
}
