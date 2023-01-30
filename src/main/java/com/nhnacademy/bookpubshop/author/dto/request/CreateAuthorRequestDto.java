package com.nhnacademy.bookpubshop.author.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 저자 생성을 위한 Dto 클래스입니다.
 *
 * @author : 여운석, 박경서, 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateAuthorRequestDto {
    @NotBlank(message = "저자 이름은 필수 항목입니다.")
    @Length(max = 200, message = "저자 이름은 200자를 넘길 수 없습니다.")
    private String authorName;

    @Length(max = 100, message = "대표작은 100글자를 넘을 수 없습니다.")
    private String mainBook;
}
