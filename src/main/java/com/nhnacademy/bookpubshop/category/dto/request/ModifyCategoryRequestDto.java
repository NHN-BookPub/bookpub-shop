package com.nhnacademy.bookpubshop.category.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 카테고리 수정을 위한 dto 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyCategoryRequestDto {

    @NotNull(message = "수정할 카테고리 번호를 기입해주세요.")
    private Integer categoryNo;

    private Integer parentCategoryNo;

    @NotBlank(message = "수정할 카테고리명을 기입해주세요")
    @Length(max = 10, message = "카테고리명의 길이가 맞지않습니다.")
    private String categoryName;

    private Integer categoryPriority;

    private boolean categoryDisplayed;

}
