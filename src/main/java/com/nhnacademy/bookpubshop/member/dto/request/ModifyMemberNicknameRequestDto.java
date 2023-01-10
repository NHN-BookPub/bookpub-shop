package com.nhnacademy.bookpubshop.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버 회원정보를 수정하기위한 DTO 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyMemberNicknameRequestDto {
    @NotBlank(message = "닉네임은 null 이 될수없습니다.")
    @Pattern(regexp = "^.*(?=.*[a-z])(?=.*[a-z\\d])(?=.{2,8}).*$",
            message = "닉네임은 영어는 필수 숫자는 선택으로 2글자 이상 8글자 이하로 입력해주세요.")
    private String nickname;
}
