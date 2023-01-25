package com.nhnacademy.bookpubshop.member.dto.response;

import com.nhnacademy.bookpubshop.member.entity.Member;
import lombok.Getter;

/**
 * 회원의 encoding 된 비밀번호가 기입됩니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
public class MemberPasswordResponseDto {
    private final String password;

    /**
     * 회원의 패스워드를 받아서 넘긴다.
     *
     * @param member 회원의 정보 기입.
     */
    public MemberPasswordResponseDto(Member member) {
        this.password = member.getMemberPwd();
    }
}
