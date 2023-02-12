package com.nhnacademy.bookpubshop.order.relationship.exception;

/**
 * 주문상품이 없을경우 발생하는 에러입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundOrderProductException extends RuntimeException {

    public static final String MESSAGE = "해당 주문상품은 존재하지 않습니다.";

    public NotFoundOrderProductException() {
        super(MESSAGE);
    }
}
