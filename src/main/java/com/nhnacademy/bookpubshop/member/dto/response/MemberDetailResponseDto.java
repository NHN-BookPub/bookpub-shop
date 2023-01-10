package com.nhnacademy.bookpubshop.member.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버가 반환될때 보내야할 정보들이 담겨져있습니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetailResponseDto {
    private Long memberNo;
    private String tierName;
    private String nickname;
    private String gender;
    private Integer birthMonth;
    private Integer birthYear;
    private String phone;
    private String email;
    private Long point;
    private String authority;
}
