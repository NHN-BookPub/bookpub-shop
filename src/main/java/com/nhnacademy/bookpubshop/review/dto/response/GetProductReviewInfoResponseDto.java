package com.nhnacademy.bookpubshop.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품의 상품평 정보들을 조회하기 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductReviewInfoResponseDto {
    private Long reviewCount;
    private Integer productStar;
}
