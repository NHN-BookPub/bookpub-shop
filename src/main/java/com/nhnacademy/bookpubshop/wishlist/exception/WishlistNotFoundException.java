package com.nhnacademy.bookpubshop.wishlist.exception;

/**
 * 위시리스트를 찾지 못했을 경우 발생할 에러.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class WishlistNotFoundException extends RuntimeException {

    public static final String MESSAGE = "첮을 수 없는 위시리스트입니다.";

    public WishlistNotFoundException() {
        super(MESSAGE);
    }
}
