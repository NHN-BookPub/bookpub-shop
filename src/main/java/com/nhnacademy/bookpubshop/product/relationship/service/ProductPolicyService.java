package com.nhnacademy.bookpubshop.product.relationship.service;

import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import java.util.List;

/**
 * 상품정책 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
public interface ProductPolicyService {
    /**
     * 상품정책을 생성하는 메서드입니다.
     *
     * @param requestDto 생성시 필요한 dto class.
     * @return 생성된 객체를 반환합니다.
     */
    void createProductPolicy(CreateModifyProductPolicyRequestDto requestDto);

    /**
     * 번호를 이용하여 상품 정책을 조회합니다.
     *
     * @param policyNo 정책번호입니다.
     * @return 조회된 정책을 반환합니다.
     */
    GetProductPolicyResponseDto getProductPolicyById(Integer policyNo);

    /**
     * 전체 상품 정책 조회합니다.
     *
     * @return 전체 상품 정책 DTO
     */
    List<GetProductPolicyResponseDto> getProductPolicies();

    /**
     * 정책번호로 조회한 후 그 정책을 수정합니다.
     *
     * @param policyNo 정책번호입니다.
     * @param policy   수정할 정책입니다.
     * @return 수정된 객체를 반환합니다.
     */
    void modifyProductPolicyById(Integer policyNo,
                                 CreateModifyProductPolicyRequestDto policy);
}
