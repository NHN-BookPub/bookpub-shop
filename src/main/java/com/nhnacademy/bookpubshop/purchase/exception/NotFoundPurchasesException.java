package com.nhnacademy.bookpubshop.purchase.exception;

/**
 * 구매이력을 찾을 수 없을 때 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundPurchasesException extends RuntimeException {
    public static final String MESSAGE = "매입이력을 찾을 수 없습니다.";

    public NotFoundPurchasesException() {
        super(MESSAGE);
    }
}
