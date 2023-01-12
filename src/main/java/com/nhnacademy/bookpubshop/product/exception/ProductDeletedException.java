package com.nhnacademy.bookpubshop.product.exception;

/**
 * 상품 상세 페이지에서 삭제된 상품은
 * 보이게 하지 않기 위한 예외 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class ProductDeletedException extends RuntimeException {
    private static final String MESSAGE = ": 삭제된 상품입니다.";

    public ProductDeletedException(Long id) {
        super(id + MESSAGE);
    }
}
