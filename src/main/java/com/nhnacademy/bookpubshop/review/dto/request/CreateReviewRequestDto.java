package com.nhnacademy.bookpubshop.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품평 등록을 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {
    private Long memberNo;
    private Long productNo;
    private Long reviewStar;
    private String reviewContent;
}
