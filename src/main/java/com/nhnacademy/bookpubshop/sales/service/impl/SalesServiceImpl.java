package com.nhnacademy.bookpubshop.sales.service.impl;

import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.SaleProductCntDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import com.nhnacademy.bookpubshop.sales.service.SalesService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 매출서비스 실 구현 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TotalSaleDto> getSales(LocalDateTime start,
                                       LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            start = LocalDateTime.now().minusMonths(1L);
            end = LocalDateTime.now();
        }
        return orderRepository.getTotalSale(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderCntResponseDto> getOrderCnt() {
        return orderRepository.getOrderTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TotalSaleYearDto> getTotalSaleCurrentYear(LocalDateTime start,
                                                          LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            start = LocalDateTime.now()
                    .withDayOfMonth(1)
                    .withMonth(1);
            end = LocalDateTime.now();
        }
        return orderRepository.getTotalSaleMonth(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SaleProductCntDto> getSaleProductCount(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            start = LocalDateTime.now()
                    .withDayOfMonth(1)
                    .withMonth(1);
            end = LocalDateTime.now();
        }
        return orderProductRepository.getSaleProductCount(start, end);
    }
}
