package com.nhnacademy.bookpubshop.personalinquiryanswer.exception;

/**
 * 1대1문의답변을 찾을 수 없을 때 나는 에러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class PersonalInquiryAnswerNotFoundException extends RuntimeException {
    public static final String MESSAGE = "존재하지 않는 1대1문의 답변입니다.";

    public PersonalInquiryAnswerNotFoundException() {
        super(MESSAGE);
    }
}
