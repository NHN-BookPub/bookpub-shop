package com.nhnacademy.bookpubshop.reviewpolicy.exception;

/**
 * 사용하고 있는 상품평 정책이 없을 경우 나오는 에러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class ReviewPolicyUsedNotFoundException extends RuntimeException {
    public static final String MESSAGE = "상품평 정책을 찾을 수 없습니다";

    public ReviewPolicyUsedNotFoundException() {
        super(MESSAGE);
    }
}
