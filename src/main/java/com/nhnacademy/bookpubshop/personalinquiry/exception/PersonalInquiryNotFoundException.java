package com.nhnacademy.bookpubshop.personalinquiry.exception;

/**
 * 1대1문의 번호가 없을 경우 발생하는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class PersonalInquiryNotFoundException extends RuntimeException {

    public static final String MESSAGE = "1대1문의를 찾을 수 없습니다.";

    public PersonalInquiryNotFoundException() {
        super(MESSAGE);
    }
}
