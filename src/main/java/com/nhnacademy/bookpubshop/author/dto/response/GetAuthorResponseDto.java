package com.nhnacademy.bookpubshop.author.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 저자를 돌려주기 위한 Dto 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetAuthorResponseDto {
    private Integer authorNo;
    private String authorName;
    private String mainBook;
}
