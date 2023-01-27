package com.nhnacademy.bookpubshop.product.exception;

/**
 * 상태코드를 찾을 수 없을 때 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundStateCodeException extends RuntimeException {
    public static final String MESSAGE = "찾을 수 없습니다.";

    public NotFoundStateCodeException() {
        super(MESSAGE);
    }
}