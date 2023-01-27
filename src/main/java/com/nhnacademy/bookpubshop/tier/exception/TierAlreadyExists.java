package com.nhnacademy.bookpubshop.tier.exception;

/**
 * 이미 등급이 존재할경우.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class TierAlreadyExists extends RuntimeException {
    public static final String MESSAGE = " 은 이미존재하는 등급입니다.";

    public TierAlreadyExists(String message) {
        super(message + MESSAGE);
    }
}
