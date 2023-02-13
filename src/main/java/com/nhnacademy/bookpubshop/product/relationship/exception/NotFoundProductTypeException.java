package com.nhnacademy.bookpubshop.product.relationship.exception;

/**
 * 상품 타입이 없는 경우 발생하는 Exception.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class NotFoundProductTypeException extends RuntimeException {
    public static final String MESSAGE = "없는 상품 타입 코드입니다.";

    public NotFoundProductTypeException() {
        super(MESSAGE);
    }
}
