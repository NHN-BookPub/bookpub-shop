package com.nhnacademy.bookpubshop.order.relationship.service;

import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * 주문상품 서비스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface OrderProductService {
    /**
     * 교환상태인 주문상품 리스트를 가져오는 메소드.
     *
     * @param pageable 페이지.
     * @return 교환상태인 주문상품 리스트.
     */
    PageResponse<GetExchangeResponseDto> getExchangeOrderList(Pageable pageable);
}
