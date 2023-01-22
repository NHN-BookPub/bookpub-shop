package com.nhnacademy.bookpubshop.token.exception;


/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class TokenParsingException extends RuntimeException {
    public static final String MESSAGE = "토큰을 읽어올 수 없습니다";

    public TokenParsingException() {
        super(MESSAGE);
    }
}
