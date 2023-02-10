package com.nhnacademy.bookpubshop.orderstatecode.exception;

/**
 * 주문 상태를 찾을 수 없을 때 발생하는 에러입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundOrderStateException extends RuntimeException {
    public static final String MESSAGE = "주문 상태를 찾을 수 없습니다";

    public NotFoundOrderStateException() {
        super(MESSAGE);
    }
}
