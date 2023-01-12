package com.nhnacademy.bookpubshop.customersupport.exception;

/**
 * 고객서비스 번호가 없을 경우 발생하는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CustomerServiceNotFoundException extends RuntimeException {
    public static final String MESSAGE = "은 없는 고객서비스 번호입니다.";

    public CustomerServiceNotFoundException(Integer serviceNo) {
        super(serviceNo + MESSAGE);
    }
}
