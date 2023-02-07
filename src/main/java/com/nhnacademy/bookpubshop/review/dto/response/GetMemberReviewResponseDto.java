package com.nhnacademy.bookpubshop.review.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원이 작성한 리뷰를 조회할때 사용하는 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberReviewResponseDto {
    private Long reviewNo;
    private Long productNo;
    private String productTitle;
    private String productPublisher;
    private List<String> productAuthorNames = new ArrayList<>();
    private String productImagePath;
    private Integer reviewStar;
    private String reviewContent;
    private String reviewImagePath;
    private LocalDateTime createdAt;

    public void setAuthorNames(List<String> authorNames) {
        this.productAuthorNames = authorNames;
    }
}
