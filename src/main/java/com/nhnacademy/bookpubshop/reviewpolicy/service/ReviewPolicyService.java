package com.nhnacademy.bookpubshop.reviewpolicy.service;

import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.CreateReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.ModifyPointReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import java.util.List;

/**
 * 상품평정책을 다루기 위한 서비스 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface ReviewPolicyService {

    /**
     * 상품평 정책을 등록하기 위한 메서드입니다.
     *
     * @param createRequestDto 상품평 정책 등록에 필요한 정보를 담은 Dto
     */
    void createReviewPolicy(CreateReviewPolicyRequestDto createRequestDto);

    /**
     * 상품평 정책 지급포인트를 수정하기 위한 메서드입니다.
     *
     * @param modifyRequestDto 상품평 정책 수정에 필요한 정보를 담은 Dto
     */
    void modifyReviewPolicy(ModifyPointReviewPolicyRequestDto modifyRequestDto);

    /**
     * 상품평 정책 사용여부를 수정하기 위한 메서드입니다.
     * 해당 상품평 정책 사용여부가 true 로 바뀌고, 다른 상품평 정책 사용여부는 false 로 바뀌는 로직을 수행합니다.
     *
     * @param policyNo 수정할 상품평 정책 번호
     */
    void modifyUsedReviewPolicy(Integer policyNo);

    /**
     * 상품평 정책 리스트 조회를 위한 메서드입니다.
     *
     * @return 상품평 정책 정보를 담은 Dto 리스트
     */
    List<GetReviewPolicyResponseDto> getReviewPolicies();
}
