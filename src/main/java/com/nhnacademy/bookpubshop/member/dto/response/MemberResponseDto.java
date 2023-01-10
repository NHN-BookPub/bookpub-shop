package com.nhnacademy.bookpubshop.member.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버 전체정보 조회시 보여할 정보들이다.
 *
 * @author : 유호철
 * @since : 1.0
 **/

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberNo;
    private String tier;
    private String memberId;
    private String nickname;
    private String name;
    private String gender;
    private Integer birthYear;
    private Integer birthMonth;
    private String email;
    private Long point;
    private boolean isSocial;
}
