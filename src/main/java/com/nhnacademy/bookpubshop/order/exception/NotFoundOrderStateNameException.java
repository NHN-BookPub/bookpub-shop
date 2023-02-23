package com.nhnacademy.bookpubshop.order.exception;

/**
 * 주문상태코드를 영어이름으로 조회할 때 결과가 없을 때 발생하는 예외
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundOrderStateNameException extends RuntimeException {
    public static final String MESSAGE = "정의되지 않은 주문 상태입니다.";

    public NotFoundOrderStateNameException() {
        super(MESSAGE);
    }
}
