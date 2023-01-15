package com.nhnacademy.bookpubshop.member.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 멤버의 반환 dto 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class LoginMemberResponseDto {
    private Long memberNo;
    private String memberId;
    private String memberPwd;
    private List<String> authorities;
}
