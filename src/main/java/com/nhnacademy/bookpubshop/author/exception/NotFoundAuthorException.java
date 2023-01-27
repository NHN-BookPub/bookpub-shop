package com.nhnacademy.bookpubshop.author.exception;

/**
 * 저자를 찾을 수 없을 때 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class NotFoundAuthorException extends RuntimeException {
    public static final String MESSAGE = "저자가 없습니다.";

    public NotFoundAuthorException() {
        super(MESSAGE);
    }
}
