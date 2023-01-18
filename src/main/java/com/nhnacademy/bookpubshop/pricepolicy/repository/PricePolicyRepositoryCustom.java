package com.nhnacademy.bookpubshop.pricepolicy.repository;

import com.nhnacademy.bookpubshop.pricepolicy.dto.GetPricePolicyResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * querydsl을 사용하기 위한 custom 레포지토리입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PricePolicyRepositoryCustom {
    /**
     * 정책번호로 조회합니다.
     *
     * @param policyNo 정책번호.
     * @return 단건 정책을 반환합니다.
     */
    Optional<GetPricePolicyResponseDto> findPolicyByNo(Integer policyNo);

    /**
     * 모든 정책들을 반환합니다.
     *
     * @return 정책리스트를 반환합니다.
     */
    List<GetPricePolicyResponseDto> findAllPolicies();
}
