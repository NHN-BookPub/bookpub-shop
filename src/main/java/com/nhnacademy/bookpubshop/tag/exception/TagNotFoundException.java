package com.nhnacademy.bookpubshop.tag.exception;

/**
 * 잘못된 태그 번호를 가지고 조회시 생기는 에러입니다.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class TagNotFoundException extends RuntimeException {

    public static final String ERROR_MESSAGE = "번 태그는 없는 태그 입니다.";

    public TagNotFoundException(Integer tagNo) {
        super(tagNo + ERROR_MESSAGE);
    }
}
