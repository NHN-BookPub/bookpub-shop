package com.nhnacademy.bookpubshop.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
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
