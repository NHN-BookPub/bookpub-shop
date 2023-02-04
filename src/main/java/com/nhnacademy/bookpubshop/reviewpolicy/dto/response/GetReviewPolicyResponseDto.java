package com.nhnacademy.bookpubshop.reviewpolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품평 정책 조회를 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetReviewPolicyResponseDto {
    private Integer policyNo;
    private Long sendPoint;
    private boolean policyUsed;
}
