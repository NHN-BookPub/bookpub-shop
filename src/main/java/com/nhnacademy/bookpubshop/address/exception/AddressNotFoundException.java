package com.nhnacademy.bookpubshop.address.exception;

/**
 * 주소가 존재하지 않을시 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class AddressNotFoundException extends RuntimeException {
    public static final  String MESSAGE = "찾을 수 없는 주소입니다.";
    public AddressNotFoundException() {
        super(MESSAGE);
    }
}
