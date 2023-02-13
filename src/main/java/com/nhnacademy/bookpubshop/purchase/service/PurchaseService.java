package com.nhnacademy.bookpubshop.purchase.service;

import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetAppliedMemberResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    PageResponse<GetPurchaseListResponseDto> getPurchaseByProductNo(
            Long productNo, Pageable pageable);

    /**
     * 매입이력을 생성합니다.
     *
     * @param request 매입이력 등록시 사용하는 dto.
     */
    void createPurchase(CreatePurchaseRequestDto request);

    /**
     * 매입이력을 수정합니다.
     *
     * @param purchaseId 매입이력번호입니다.
     * @param request    수정시 사용하는 dto.
     */
    void modifyPurchase(Long purchaseId, CreatePurchaseRequestDto request);

    /**
     * 최신순으로 매입이력을 조회합니다.
     *
     * @param pageable 페이징.
     * @return 페이징된 매입이력들을 반환합니디.
     */
    Page<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable);

    /**
     * 매입이력 등록시 상품의 재고가 함께 증가됩니다.
     *
     * @param request 생성시 dto 입니다.
     * @return 위시리스트에서 알림을 등록한 사용ㅇ자 리스트
     */
    List<GetAppliedMemberResponseDto> createPurchaseMerged(CreatePurchaseRequestDto request);
}
