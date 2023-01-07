package com.nhnacademy.bookpubshop.tag.dto.request;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 태그 수정을 위한 DTO 객체입니다..
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyTagRequestDto {

    private Integer tagNo;


    @Length(min = 1, max = 20, message = "태그 이름은 최소 1글자, 최대 20글자 가능헙니다.")
    private String tagName;

    @Pattern(regexp = "^#([a-fA-F0-9]{6})$", message = "지원하지 않는 색상 코드입니다.")
    private String colorCode;
}
