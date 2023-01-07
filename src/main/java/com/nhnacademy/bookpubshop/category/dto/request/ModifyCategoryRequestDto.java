package com.nhnacademy.bookpubshop.category.dto.request;

import com.nhnacademy.bookpubshop.category.entity.Category;
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

    @NotNull
    private Integer categoryNo;

    private Category parentCategory;

    @NotBlank(message = "수정할 카테고리명을 기입해주세요")
    @Length(max = 10, message = "카테고리명의 길이가 맞지않습니다.")
    private String categoryName;

    @NotBlank(message = "수정할 카테고리 우선순위를 기입해주세요")
    private Integer categoryPriority;

    @NotBlank(message = "수정할 카테고리 노출여부를 기입해주세요")
    private boolean categoryDisplayed;

}
