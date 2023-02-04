package com.nhnacademy.bookpubshop.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductReviewInfoResponseDto {
    private Long reviewCount;
    private Long productStar;
}
