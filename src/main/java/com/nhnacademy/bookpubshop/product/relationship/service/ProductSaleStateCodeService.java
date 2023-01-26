package com.nhnacademy.bookpubshop.product.relationship.service;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import java.util.List;

/**
 * 상품판매상태코드 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
public interface ProductSaleStateCodeService {

    /**
     * 코드번호로 상품판매유형코드를 반환합니다.
     *
     * @param id 코드번호입니다.
     * @return 상품판매유형코드 dto를 반환합니다.
     */
    GetProductSaleStateCodeResponseDto getSaleCodeById(Integer id);

    /**
     * 상품판매유형코드의 사용여부를 수정합니다.
     *
     * @param id   코드번호입니다.
     * @param used 사용여부입니다.
     * @return 수정된 코드를 반환합니다.
     */
    GetProductSaleStateCodeResponseDto setUsedSaleCodeById(Integer id, boolean used);

    /**
     * 모든 상품판매유형코드를 조회합니다.
     *
     * @return 모든 상품판매유형코드를 반환합니다.
     */
    List<GetProductSaleStateCodeResponseDto> getAllProductSaleStateCode();

    /**
     * 사용중인 상품 판매 사태 코드를 조회합니다.
     *
     * @return 사용중인 모든 상품 판매 유형
     */
    List<GetProductSaleStateCodeResponseDto> getAllProductSaleStateCodeUsed();
}
