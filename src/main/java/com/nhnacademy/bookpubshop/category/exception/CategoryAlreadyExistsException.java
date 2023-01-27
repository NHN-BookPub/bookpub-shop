package com.nhnacademy.bookpubshop.category.exception;

/**
 * 카테고리가 이미 존재하여 예외 발생.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class CategoryAlreadyExistsException extends RuntimeException {

    public static final String MESSAGE = " 은 이미 존재하는 카테고리입니다.";

    public CategoryAlreadyExistsException(String categoryName) {
        super(categoryName + MESSAGE);

    }
}
