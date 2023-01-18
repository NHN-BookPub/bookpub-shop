package com.nhnacademy.bookpubshop.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버의 이름을 수정할때 쓰이는 DTO 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyMemberNameRequestDto {

    @NotBlank(message = "빈값은 이름이 될수없습니다.")
    @Pattern(regexp = "^.*(?=.*[가-힣a-z])(?=.{2,200}).*$",
            message = "이름은 한글 또는 영어 2글자 이상 200글자 이하로 입력해주세요.")
    private String name;

}
