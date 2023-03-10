package com.nhnacademy.bookpubshop.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 메인페이지에서 하위 카테고리 정보를 반환하는 dto 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetChildCategoryResponseDto {

    private Integer categoryNo;
    private String categoryName;

}
