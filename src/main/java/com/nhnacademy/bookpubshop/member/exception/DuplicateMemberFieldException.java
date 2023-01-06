package com.nhnacademy.bookpubshop.member.exception;

/**
 * member 필드가 db에 중복되어 있을경우 발생하는 exception 클래스입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class DuplicateMemberFieldException extends RuntimeException{
    public DuplicateMemberFieldException(String message) {
        super(message+"는 중복되는 항목입니다.");
    }
}

