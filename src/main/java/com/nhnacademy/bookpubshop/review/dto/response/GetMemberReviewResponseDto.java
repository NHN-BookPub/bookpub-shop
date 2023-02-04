package com.nhnacademy.bookpubshop.review.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
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
    private Long reviewStar;
    private String reviewContent;
    private String reviewImagePath;
    private LocalDateTime createdAt;

    public void setAuthorNames(List<String> authorNames) {
        this.productAuthorNames = authorNames;
    }
}
