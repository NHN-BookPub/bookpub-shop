package com.nhnacademy.bookpubshop.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 유저의 로그인 정보가 들어오는 DTO.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class LoginMemberRequestDto {
    private String memberId;
    private String password;
}
