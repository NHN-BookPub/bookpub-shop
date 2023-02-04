package com.nhnacademy.bookpubshop.review.exception;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class ReviewNotFoundException extends RuntimeException {
    public static final String MESSAGE = "번은 없는 리뷰 번호입니다.";

    public ReviewNotFoundException(Long reviewNo) {
        super(reviewNo + MESSAGE);
    }
}