package com.nhnacademy.bookpubshop.product.exception;

/**
 * 상품 번호를 이용하여 상품을 찾을 수 없을 때
 * 발생하는 예외 클래스 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class ProductNotFoundException extends RuntimeException {
    public static final String MESSAGE = ": 찾을 수 없습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
