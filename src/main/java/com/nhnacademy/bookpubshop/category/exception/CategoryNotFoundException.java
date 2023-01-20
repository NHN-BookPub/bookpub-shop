package com.nhnacademy.bookpubshop.category.exception;

/**
 * 카테고리가 존재하지 않아 예외 발생.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class CategoryNotFoundException extends RuntimeException {

    public static final String MESSAGE = "카테고리가 존재하지않습니다.";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }
}
