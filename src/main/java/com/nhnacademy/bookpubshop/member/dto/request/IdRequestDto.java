package com.nhnacademy.bookpubshop.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 아이디 중복체크 확인 DTO.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IdRequestDto {
    private String id;
}
