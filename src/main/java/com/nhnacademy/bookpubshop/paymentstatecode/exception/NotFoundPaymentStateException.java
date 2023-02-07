package com.nhnacademy.bookpubshop.paymentstatecode.exception;

/**
 * 해당 상태가 없으면 발생하는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundPaymentStateException extends RuntimeException {
    public static final String MESSAGE = "해당 타입의 결제상태는 존재하지않습니다.";

    public NotFoundPaymentStateException() {
        super(MESSAGE);
    }
}
