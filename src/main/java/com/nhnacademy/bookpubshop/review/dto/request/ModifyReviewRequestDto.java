package com.nhnacademy.bookpubshop.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품평 수정을 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyReviewRequestDto {
    private Long reviewStar;
    private String reviewContent;
}
