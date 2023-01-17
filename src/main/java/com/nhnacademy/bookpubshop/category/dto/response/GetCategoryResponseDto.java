package com.nhnacademy.bookpubshop.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 기본정보를 반환하기위한 DTO 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryResponseDto {

    private Integer categoryNo;

    private String categoryName;

    private GetCategoryResponseDto parent;

    private Integer categoryPriority;

    private boolean categoryDisplayed;

    public GetCategoryResponseDto(Integer categoryNo, String categoryName) {
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
    }

    public GetCategoryResponseDto(Integer categoryNo, String categoryName, Integer categoryPriority,
                                  boolean categoryDisplayed) {
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
        this.categoryPriority = categoryPriority;
        this.categoryDisplayed = categoryDisplayed;
    }
}
