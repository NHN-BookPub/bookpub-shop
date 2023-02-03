package com.nhnacademy.bookpubshop.pricepolicy.service;

import com.nhnacademy.bookpubshop.pricepolicy.dto.request.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.util.List;

/**
 * 가격정책 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface PricePolicyService {
    /**
     * 가격정책을 등록합니다.
     *
     * @param request 등록을 위한 dto.
     */
    void createPricePolicy(CreatePricePolicyRequestDto request);

    /**
     * 정책번호로 가격을 수정합니다.
     *
     * @param pricePolicyNo 정책번호.
     * @param fee 수정할 가격.
     */
    void modifyPricePolicyFee(Integer pricePolicyNo, Long fee);

    /**
     * 가격정책을 번호로 조회합니다.
     *
     * @param pricePolicyName 정책이름입니다.
     * @return 정책을 반환합니다.
     */
    List<GetPricePolicyResponseDto> getPricePoliciesByName(String pricePolicyName);

    /**
     * 모든 가격정책을 반환합니다.
     *
     * @return 가격정책 리스트를 반환합니다.
     */
    List<GetPricePolicyResponseDto> getPricePolicies();

    /**
     * 가장 최근의 가격정책을 이름으로 조회합니다.
     *
     * @param name 정책명.
     * @return 단건 반환.
     */
    PricePolicy getLatestPricePolicyByName(String name);

    /**
     * 주문서에 필요한 배송비, 포장비 정책을 가져오는 메소드입니다..
     *
     * @return 배송비, 포장비 정책.
     */
    List<GetOrderPolicyResponseDto> getOrderRequestPolicy();
}
