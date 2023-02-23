package com.nhnacademy.bookpubshop.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요 현황 카테고리별 조회를 위한 Dto.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryInfoResponseDto {

    private Integer categoryNo;

    private String categoryName;

}
