package com.nhnacademy.bookpubshop.member.exception;

/**
 * 이미 존재하는 아이디일때 발생하는 에러입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class IdAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE = "은 이미 사용중인 아이디입니다.";

    public IdAlreadyExistsException(String message) {
        super(message + MESSAGE);

    }
}
