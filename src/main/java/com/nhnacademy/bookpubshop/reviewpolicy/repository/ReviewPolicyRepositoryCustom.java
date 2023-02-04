package com.nhnacademy.bookpubshop.reviewpolicy.repository;

import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import java.util.List;

/**
 * 상품평 정책 custom 레포지토리.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface ReviewPolicyRepositoryCustom {

    /**
     * 상품평 정책 리스트 조회를 위한 메서드입니다.
     *
     * @return 상품평 정책 정보를 담은 Dto 리스트
     */
    List<GetReviewPolicyResponseDto> findReviewPolicies();

}
