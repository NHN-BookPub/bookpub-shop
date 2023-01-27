package com.nhnacademy.bookpubshop.tag.dto.request;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 태그 등록을 위한 DTO 객체입니다.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class AddTagRequestDto {

    @Length(min = 1, max = 20, message = "태그 이름은 최소 1글자, 최대 20글자 가능합니다.")
    private String addTagName;

    @Pattern(regexp = "#([a-fA-F0-9]{6})", message = "지원하지 않는 색상 코드입니다.")
    private String addColorCode;
}
