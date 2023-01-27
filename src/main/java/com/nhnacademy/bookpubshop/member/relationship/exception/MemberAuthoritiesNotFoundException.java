package com.nhnacademy.bookpubshop.member.relationship.exception;

/**
 * 멤버권한정보를 찾을 수 없을 때 발생하는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class MemberAuthoritiesNotFoundException extends RuntimeException{
    public static final String MESSAGE = "멤버의 권한을 찾을 수 없습니다.";

    public MemberAuthoritiesNotFoundException() {
        super(MESSAGE);
    }
}
