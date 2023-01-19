package com.nhnacademy.bookpubshop.pricepolicy.exception;

/**
 * 상품정책이 존재하지 않을 때 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundPricePolicyException extends RuntimeException{
    public static final String MESSAGE = "존재하지 않는 정책입니다.";

    public NotFoundPricePolicyException() {
        super(MESSAGE);
    }
}
