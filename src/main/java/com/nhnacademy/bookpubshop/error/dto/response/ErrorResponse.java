package com.nhnacademy.bookpubshop.error.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러를 핸들링하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String code;
}
