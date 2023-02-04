package com.nhnacademy.bookpubshop.review.dto.response;

import java.time.LocalDateTime;
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
public class GetProductReviewResponseDto {
    private Long reviewNo;
    private String memberNickname;
    private Long reviewStar;
    private String reviewContent;
    private String imagePath;
    private LocalDateTime createdAt;
}
