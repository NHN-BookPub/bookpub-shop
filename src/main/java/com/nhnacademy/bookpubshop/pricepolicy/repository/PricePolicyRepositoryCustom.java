package com.nhnacademy.bookpubshop.pricepolicy.repository;

import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * querydsl을 사용하기 위한 custom 레포지토리입니다.
 *
 * @author : 여운석, 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PricePolicyRepositoryCustom {
    /**
     * 정책이름으로 조회합니다.
     *
     * @param policyName 정책이름.
     * @return 단건 정책을 반환합니다.
     */
    List<GetPricePolicyResponseDto> getPricePolicyByName(String policyName);

    /**
     * 정책번호로 조회합니다.
     *
     * @param policyNo 정책번호
     * @return 단건 정책을 반환합니다.
     */
    Optional<PricePolicy> getPricePolicyById(Integer policyNo);

    /**
     * 모든 정책들을 반환합니다.
     *
     * @return 정책리스트를 반환합니다.
     */
    List<GetPricePolicyResponseDto> findAllPolicies();

    /**
     * 정책 이름으로 가장 최근 정책을 반환합니다.
     *
     * @param name 정책명입니다.
     * @return 찾은 정책입니다.
     */
    Optional<PricePolicy> getLatestPricePolicyByName(String name);

    /**
     * 포장비, 배송비의 가장 최근 정책을 반환합니다.
     *
     * @return 포장비, 배송비 정책
     */
    List<GetOrderPolicyResponseDto> getShipAndPackagePolicy();
}
