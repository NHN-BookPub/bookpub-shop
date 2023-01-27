package com.nhnacademy.bookpubshop.tier.exception;

/**
 * 등급 정보를 찾을수 없을 때 발생되는 exception입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotFoundTierException extends RuntimeException {

    public static final String MESSAGE = "존재하지 않는 등급입니다.";
    public NotFoundTierException() {
        super(MESSAGE);
    }
}
