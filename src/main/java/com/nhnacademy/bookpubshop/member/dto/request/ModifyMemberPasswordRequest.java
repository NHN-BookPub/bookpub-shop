package com.nhnacademy.bookpubshop.member.dto.request;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버의 패스워드 변경시 사용되는 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyMemberPasswordRequest {
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,50}$",
    message = "패스워드가 규격에 적합하지 않습니다.")
    private String password;
}
