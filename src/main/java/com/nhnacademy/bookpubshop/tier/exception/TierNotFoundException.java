package com.nhnacademy.bookpubshop.tier.exception;

/**
 * 등급이 존재하지않을때 예외.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class TierNotFoundException extends RuntimeException {

    public static final String MESSAGE = "등급이 존재하지않습니다.";

    public TierNotFoundException() {
        super(MESSAGE);
    }
}
