package com.nhnacademy.bookpubshop.member.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증 성공한 유저의 반환 dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberAuthResponseDto {
    private Long memberNo;
    private String memberPwd;
    private List<String> authorities;
}
