package com.nhnacademy.bookpubshop.author.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 저자 생성을 위한 Dto 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateAuthorRequestDto {
    private String authorName;
}
