package com.nhnacademy.bookpubshop.order.exception;

/**
 * 주문 번호로 조회시 주문이 존재하지 않으면 반환하는 예외클래스.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class OrderNotFoundException extends RuntimeException {
    public static final String MESSAGE = "찾을 수 없는 주문입니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
