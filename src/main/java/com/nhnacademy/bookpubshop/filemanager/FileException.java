package com.nhnacademy.bookpubshop.filemanager;

/**
 * 파일 관련 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class FileException extends RuntimeException {
    public static final String MESSAGE = "파일을 저장하는 도중 에러 발생";

    public FileException() {
        super(MESSAGE);
    }
}
