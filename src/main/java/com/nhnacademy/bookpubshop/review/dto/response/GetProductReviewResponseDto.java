package com.nhnacademy.bookpubshop.review.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품에 관한 상품평들을 조회하기 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductReviewResponseDto {
    private Long reviewNo;
    private String memberNickname;
    private Integer reviewStar;
    private String reviewContent;
    private String imagePath;
    private LocalDateTime createdAt;
}
