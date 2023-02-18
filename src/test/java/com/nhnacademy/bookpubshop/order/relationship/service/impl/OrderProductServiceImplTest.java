package com.nhnacademy.bookpubshop.order.relationship.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.service.OrderProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 주문상품 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/

class OrderProductServiceImplTest {
    @Autowired
    OrderProductService orderProductService;

    OrderProductRepository orderProductRepository;
    GetExchangeResponseDto dto;

    @BeforeEach
    void setUp() {
        orderProductRepository = Mockito.mock(OrderProductRepository.class);
        orderProductService = new OrderProductServiceImpl(orderProductRepository);

        dto = new GetExchangeResponseDto(
                1L,
                "memberId",
                1L,
                "title",
                "thumbnail",
                1,
                "stateCode",
                "exchangeReason"
        );
    }

    @Test
    void getExchangeOrderList() {
        List<GetExchangeResponseDto> dtos = new ArrayList<>();
        dtos.add(dto);

        Pageable pageable = Pageable.ofSize(10);
        Page<GetExchangeResponseDto> pages = PageableExecutionUtils.getPage(dtos, pageable, dtos::size);

        when(orderProductRepository.getExchangeOrderProductList(any()))
                .thenReturn(pages);

        orderProductService.getExchangeOrderList(pageable);

        verify(orderProductRepository, times(1)).getExchangeOrderProductList(any());
    }
}