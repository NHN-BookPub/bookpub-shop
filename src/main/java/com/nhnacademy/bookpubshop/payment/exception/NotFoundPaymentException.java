package com.nhnacademy.bookpubshop.payment.exception;

/**
 * 결제 정보를 찾을 수 없을 때 발생하는 에러입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundPaymentException extends RuntimeException {
    public static final String MESSAGE = "결제 정보를 찾을 수 없습니다.";

    public NotFoundPaymentException() {
        super(MESSAGE);
    }
}
