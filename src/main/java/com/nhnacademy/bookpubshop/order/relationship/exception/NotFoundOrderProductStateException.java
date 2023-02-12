package com.nhnacademy.bookpubshop.order.relationship.exception;

/**
 * 주문상품 상태코드를 찾지못할 경우 발생하는 exception.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundOrderProductStateException extends RuntimeException {
    public static final String MESSAGE = "해당 상태는 없는 상태코드입니다.";

    public NotFoundOrderProductStateException() {
        super(MESSAGE);
    }
}
