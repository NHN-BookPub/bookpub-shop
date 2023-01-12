package com.nhnacademy.bookpubshop.reviewpolicy.exception;

/**
 * 리뷰를 찾을 수 없을 때 나는 exception.
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
