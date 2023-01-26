package com.nhnacademy.bookpubshop.category.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 카테고리 등록을 위한 dto 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateCategoryRequestDto {

    @NotBlank(message = "등록할 카테고리명을 기입해주세요.")
    @Length(max = 10, message = "카테고리명의 길이가 맞지않습니다.")
    private String categoryName;

    private Integer parentCategoryNo;

    private Integer categoryPriority;

    private boolean categoryDisplayed;

}
