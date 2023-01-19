package com.nhnacademy.bookpubshop.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * front에서 온 요청에 대한 응답 DTO.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Getter
@AllArgsConstructor
public class SignUpMemberResponseDto {
    private String memberId;
    private String memberNickname;
    private String memberEmail;
    private String tierName;
}
