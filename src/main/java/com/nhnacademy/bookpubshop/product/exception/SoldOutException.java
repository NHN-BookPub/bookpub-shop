package com.nhnacademy.bookpubshop.product.exception;

/**
 * 상품의 재고가 부족하거나 품절일 시 발생하는 예외입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class SoldOutException extends RuntimeException {
    public static final String MESSAGE = "All Sold out, Have no more stock;";

    public SoldOutException() {
        super(MESSAGE);
    }
}
