package com.nhnacademy.bookpubshop.product.exception;

/**
 * 상품정책을 찾을 수 없을 때 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundProductPolicyException extends RuntimeException {
    public static final String MESSAGE = "존재하지 않는 정책입니다.";

    public NotFoundProductPolicyException() {
        super(MESSAGE);
    }
}
