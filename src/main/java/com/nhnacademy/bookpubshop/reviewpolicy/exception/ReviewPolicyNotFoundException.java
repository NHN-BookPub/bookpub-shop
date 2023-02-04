package com.nhnacademy.bookpubshop.reviewpolicy.exception;

/**
 * 상품평 정책을 찾을 수 없을 때 나는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class ReviewPolicyNotFoundException extends RuntimeException {
    public static final String MESSAGE = "번은 없는 성품평 정책 번호입니다.";

    public ReviewPolicyNotFoundException(Integer policyNo) {
        super(policyNo + MESSAGE);
    }
}
