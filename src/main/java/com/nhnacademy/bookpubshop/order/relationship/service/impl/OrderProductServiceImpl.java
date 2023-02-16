package com.nhnacademy.bookpubshop.order.relationship.service.impl;

import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.service.OrderProductService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 주문상품 서비스 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetExchangeResponseDto> getExchangeOrderList(Pageable pageable) {
        Page<GetExchangeResponseDto> exchange =
                orderProductRepository.getExchangeOrderProductList(pageable);
        return new PageResponse<>(exchange);
    }
}
