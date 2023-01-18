package com.nhnacademy.bookpubshop.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프론트에서 중복체크를 위해 요청한 닉네임.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NickRequestDto {
    private String nickname;
}
