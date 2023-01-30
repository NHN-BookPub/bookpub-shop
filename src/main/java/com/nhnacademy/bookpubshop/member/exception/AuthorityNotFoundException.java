package com.nhnacademy.bookpubshop.member.exception;

/**
 * 권한을 찾을 수 없을 때 나오는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AuthorityNotFoundException extends RuntimeException {
    public static final String MESSAGE = "란 이름의 권한은 존재하지 않습니다.";

    public AuthorityNotFoundException(String message) {
        super(message + MESSAGE);
    }
}
