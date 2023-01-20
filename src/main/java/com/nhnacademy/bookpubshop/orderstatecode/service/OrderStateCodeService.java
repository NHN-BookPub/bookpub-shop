package com.nhnacademy.bookpubshop.orderstatecode.service;

import com.nhnacademy.bookpubshop.orderstatecode.dto.CreateOrderStateCodeRequestDto;
import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import java.util.List;

/**
 * 주문상태코드 서비스.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderStateCodeService {
    /**
     * 주문상태코드를 등록합니다.
     *
     * @param request dto.
     */
    void createPricePolicy(CreateOrderStateCodeRequestDto request);

    /**
     * 주문상태 번호로 단일 조회.
     *
     * @param pricePolicyNo 코드번호입니다.
     * @return 주문상태 반환.
     */
    GetOrderStateCodeResponseDto getOrderStateCodeById(Integer pricePolicyNo);

    /**
     * 모든 주문상태코드를 반환합니다.
     *
     * @return 전체 주문상태코드 리스트.
     */
    List<GetOrderStateCodeResponseDto> getOrderStateCodes();

    /**
     * 사용여부를 수정합니다.
     *
     * @param codeNo 코드번호.
     */
    void modifyOrderStateCodeUsed(Integer codeNo);
}
