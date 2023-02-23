package com.nhnacademy.bookpubshop.sales.service;

import static com.nhnacademy.bookpubshop.sales.dummy.TotalSaleDummy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import com.nhnacademy.bookpubshop.sales.service.impl.SalesServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 매출현황 테스트
 *
 * @author : 유호철
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(SalesServiceImpl.class)
class SalesServiceTest {
    @Autowired
    SalesService service;

    @MockBean
    OrderRepository repository;
    @MockBean
    OrderProductRepository orderProductRepository;
    TotalSaleDto totalSaleDto;
    OrderCntResponseDto orderCntResponseDto;
    TotalSaleYearDto totalSaleYearDto;

    @BeforeEach
    void setUp() {

        totalSaleDto = totalSaleDummy();
        orderCntResponseDto = orderCntDummy();
        totalSaleYearDto = totalSaleYearDummy();
    }

    @DisplayName("한달내의 매출추이를 보여주는 테스트")
    @Test
    void getSalesTest() {
        LocalDateTime start = LocalDateTime.now().minusMonths(1L);
        LocalDateTime end = LocalDateTime.now();

        when(repository.getTotalSale(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(totalSaleDto));

        List<TotalSaleDto> result
                = service.getSales(start, end);

        assertThat(result.get(0).getSaleCnt()).isEqualTo(totalSaleDto.getSaleCnt());
        assertThat(result.get(0).getCancelPaymentCnt()).isEqualTo(totalSaleDto.getCancelPaymentCnt());
        assertThat(result.get(0).getCancelPaymentAmount()).isEqualTo(totalSaleDto.getCancelPaymentAmount());
        assertThat(result.get(0).getCancelOrderCnt()).isEqualTo(totalSaleDto.getCancelOrderCnt());
        assertThat(result.get(0).getSaleAmount()).isEqualTo(totalSaleDto.getSaleAmount());
        assertThat(result.get(0).getTotal()).isEqualTo(totalSaleDto.getTotal());
    }

    @DisplayName("시간별 주문 추이 조회")
    @Test
    void getOrderCntTest() {
        when(repository.getOrderTime())
                .thenReturn(List.of(orderCntResponseDto));

        List<OrderCntResponseDto> result = service.getOrderCnt();

        assertThat(result.get(0).getOrderCnt()).isEqualTo(orderCntResponseDto.getOrderCnt());
        assertThat(result.get(0).getDate()).isEqualTo(orderCntResponseDto.getDate());
    }

    @DisplayName("년별 조회")
    @Test
    void getTotalSaleCurrentYearTest() {
        LocalDateTime start = LocalDateTime.now()
                .withDayOfMonth(1)
                .withMonth(1);
        LocalDateTime end = LocalDateTime.now();

        when(repository.getTotalSaleMonth(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(totalSaleYearDto));

        List<TotalSaleYearDto> result = service.getTotalSaleCurrentYear(start, end);

        assertThat(result.get(0).getTotal()).isEqualTo(totalSaleYearDto.getTotal());
        assertThat(result.get(0).getSaleCnt()).isEqualTo(totalSaleYearDto.getSaleCnt());
        assertThat(result.get(0).getSaleAmount()).isEqualTo(totalSaleYearDto.getSaleAmount());
        assertThat(result.get(0).getMonth()).isEqualTo(totalSaleYearDto.getMonth());
        assertThat(result.get(0).getCancelPaymentCnt()).isEqualTo(totalSaleYearDto.getCancelPaymentCnt());
        assertThat(result.get(0).getCancelPaymentAmount()).isEqualTo(totalSaleYearDto.getCancelPaymentAmount());
        assertThat(result.get(0).getCancelOrderCnt()).isEqualTo(totalSaleYearDto.getCancelOrderCnt());
    }
}