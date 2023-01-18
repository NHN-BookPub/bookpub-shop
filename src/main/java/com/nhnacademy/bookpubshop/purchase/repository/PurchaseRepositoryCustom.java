package com.nhnacademy.bookpubshop.purchase.repository;

import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 구매이력 레포지토리에서 쿼리 dsl을 사용하기 위한 custom 레포입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PurchaseRepositoryCustom {
    /**
     * 상품 번호로 매입이력을 조회합니다.
     *
     * @param productNo 상품번호.
     * @param pageable  페이저블.
     * @return 페이징된 구매이력리스트를 반환.
     */
    Page<GetPurchaseListResponseDto> findByProductNumberWithPage(
            Long productNo, Pageable pageable);

    /**
     * 전체 매입이력을 조회합니다.
     *
     * @param pageable 페이징.
     * @return 페이징된 구매이력리스트를 반환.
     */
    Page<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable);
}
