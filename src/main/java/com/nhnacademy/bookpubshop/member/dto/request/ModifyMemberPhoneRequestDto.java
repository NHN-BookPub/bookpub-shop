package com.nhnacademy.bookpubshop.member.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 회원의 휴대전화를 수정할때 쓰이는 DTO 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyMemberPhoneRequestDto {
    @NotBlank(message = "빈값은 들어갈수없습니다.")
    @Length(min = 11, max = 11, message = "전화번호는 숫자 11글자로 입력해주세요.")
    private String phone;
}
