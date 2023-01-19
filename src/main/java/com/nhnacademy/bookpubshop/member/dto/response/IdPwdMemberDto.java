package com.nhnacademy.bookpubshop.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 멤버 테이블에서 아이디와 비밀번호만 빼오는 DTO.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@AllArgsConstructor
@Getter
public class IdPwdMemberDto {
    private Long memberNo;
    private String memberId;
    private String memberPwd;
}
