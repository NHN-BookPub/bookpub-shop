package com.nhnacademy.bookpubshop.tier.exception;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class MemberNotFoundException extends RuntimeException {
    public static final String MESSAGE = "관련 멤버를 찾을수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
