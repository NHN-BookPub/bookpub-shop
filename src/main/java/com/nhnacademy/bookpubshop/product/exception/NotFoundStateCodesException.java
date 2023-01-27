package com.nhnacademy.bookpubshop.product.exception;

/**
 * 아무 상태 코드도 없을 시 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundStateCodesException extends RuntimeException {
    public static final String MESSAGE = "어떤 코드도 존재하지 않습니다.";

    public NotFoundStateCodesException() {
        super(MESSAGE);
    }
}
