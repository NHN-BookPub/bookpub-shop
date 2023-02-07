package com.nhnacademy.bookpubshop.card.exception;

/**
 * 지원하지 않는 카드회사 코드일 경우 발생하는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotSupportedCompanyException extends RuntimeException {
    public static final String MESSAGE = "지원하지 않는 카드회사 코드입니다.";

    public NotSupportedCompanyException() {
        super(MESSAGE);
    }
}
