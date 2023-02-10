package com.nhnacademy.bookpubshop.paymenttypestatecode.exception;

/**
 * 해당 타입이 없으면 발생하는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundPaymentTypeException extends RuntimeException {
    public static final String MESSAGE = "해당 타입의 결제유형은 지원하지않습니다.";

    public NotFoundPaymentTypeException() {
        super(MESSAGE);
    }
}
