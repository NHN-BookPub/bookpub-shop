package com.nhnacademy.bookpubshop.member.exception;

/**
 * 닉네임이 이미존재했을경우 발생하는 에러입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class NicknameAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE = "는 이미 존재하는 닉네임 입니다.";

    public NicknameAlreadyExistsException(String message) {
        super(message + MESSAGE);
    }
}
