package com.nhnacademy.bookpubshop.inquirystatecode.exception;

/**
 * 상품문의상태코드가 없을 경우 나는 에러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class InquiryStateCodeNotFoundException extends RuntimeException {
    public static final String MESSAGE = "해당 문의상품코드는 없는 코드입니다.";

    public InquiryStateCodeNotFoundException() {
        super(MESSAGE);
    }
}
