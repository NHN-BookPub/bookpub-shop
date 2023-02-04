package com.nhnacademy.bookpubshop.tier.relationship.exception;

/**
 * 등급 쿠폰이 존재 하지 않을 때 예외.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class NotFoundTierCouponException extends RuntimeException {

    public static final String MESSAGE = "등급 쿠폰이 존재하지 않습니다.";

    public NotFoundTierCouponException() {
        super(MESSAGE);
    }
}
