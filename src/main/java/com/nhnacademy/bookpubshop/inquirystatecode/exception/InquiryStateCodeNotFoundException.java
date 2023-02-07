package com.nhnacademy.bookpubshop.inquirystatecode.exception;

/**
 * Some description here.
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
