package com.nhnacademy.bookpubshop.inquiry.exception;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class InquiryNotFoundException extends RuntimeException {
    public static final String MESSAGE = "해당 상품문의를 찾을 수 없습니다.";

    public InquiryNotFoundException() {
        super(MESSAGE);
    }
}
