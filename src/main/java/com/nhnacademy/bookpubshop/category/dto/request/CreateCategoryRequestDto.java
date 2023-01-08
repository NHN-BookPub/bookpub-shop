package com.nhnacademy.bookpubshop.category.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCategoryRequestDto {

    @NotBlank(message = "등록할 카테고리명을 기입해주세요.")
    @Length(max = 10, message = "카테고리명의 길이가 맞지않습니다.")
    private String categoryName;

    private Integer parentCategoryNo;

    @NotNull(message = "카테고리 우선순위는 필수 사항입니다.")
    private Integer categoryPriority;

    @NotNull(message = "카테고리 노출여부는 필수 사항입니다.")
    private boolean categoryDisplayed;

}
