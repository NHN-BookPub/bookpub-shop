package com.nhnacademy.bookpubshop.file.exception;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class FileNotFoundException extends RuntimeException {
    public static final String MESSAGE = "파일을 찾을 수 없습니다.";

    public FileNotFoundException() {
        super(MESSAGE);
    }
}
