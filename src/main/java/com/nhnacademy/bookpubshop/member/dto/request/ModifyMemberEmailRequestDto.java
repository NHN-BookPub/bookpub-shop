package com.nhnacademy.bookpubshop.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 수정할 멤버의 이메일 정보가 들어갑니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyMemberEmailRequestDto {
    private String email;
}
