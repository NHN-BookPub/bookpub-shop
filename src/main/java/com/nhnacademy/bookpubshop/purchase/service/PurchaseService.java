package com.nhnacademy.bookpubshop.purchase.service;

import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseResponseDto;
import com.nhnacademy.bookpubshop.purchase.dto.SavePurchaseRequestDto;
import java.util.List;

/**
 * 매입이력 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
public interface PurchaseService {
    /**
     * 상품 번호로 매입 이력을 조회합니다.
     *
     * @param productNo 상품번호입니다.
     * @return 모든 매입 이력을 반환합니다.
     */
    List<GetPurchaseResponseDto> getPurchaseByProductNo(Long productNo);

    /**
     * 매입이력을 생성합니다.
     *
     * @param request 매입이력 등록시 사용하는 dto.
     * @return 생성된 매입이력을 반환합니다.
     */
    GetPurchaseResponseDto createPurchase(SavePurchaseRequestDto request);

    /**
     * 매입이력을 수정합니다.
     *
     * @param purchaseId 매입이력번호입니다.
     * @param request 수정시 사용하는 dto.
     * @return 수정된 매입이력을 반환합니다.
     */
    GetPurchaseResponseDto modifyPurchase(Long purchaseId, SavePurchaseRequestDto request);
}
