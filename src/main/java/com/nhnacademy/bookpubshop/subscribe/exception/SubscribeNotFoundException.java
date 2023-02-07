package com.nhnacademy.bookpubshop.subscribe.exception;

/**
 * 구독상품을 찾을수 없을경우 발생하는 Exception 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class SubscribeNotFoundException extends RuntimeException {
    public static final String MESSAGE = "구독 상품을 찾을 수 없습니다.";

    public SubscribeNotFoundException() {
        super(MESSAGE);
    }
}
