package com.nhnacademy.bookpubshop.tag.exception;

/**
 * 태그 이름 중복시 생기는 에러입니다.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class TagNameDuplicatedException extends RuntimeException {

    public static final String ERROR_MESSAGE = " 태그 이름이 중복입니다.";

    public TagNameDuplicatedException(String tagName) {
        super(tagName + ERROR_MESSAGE);
    }
}
