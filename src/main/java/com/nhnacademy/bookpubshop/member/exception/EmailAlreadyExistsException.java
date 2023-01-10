package com.nhnacademy.bookpubshop.member.exception;

/**
 * 이미 존재하는 이메일 일때 발생하는 에러 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class EmailAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE = "는 이미 존재하는 이메일 입니다";

    public EmailAlreadyExistsException(String message) {
        super(message + MESSAGE);
    }
}
