package com.nhnacademy.bookpubshop.order.relationship.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.order.relationship.dto.CreateOrderProductStateCodeRequestDto;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.relationship.service.impl.OrderProductStateCodeServiceImpl;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 주문상품상태코드 서비스 테스트입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class OrderProductStateCodeServiceTest {
    OrderProductStateCodeRepository orderProductStateCodeRepository;
    OrderProductStateCodeService orderProductStateCodeService;
    OrderProductStateCode orderProductStateCode;
    CreateOrderProductStateCodeRequestDto requestDto;
    GetOrderProductStateCodeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        orderProductStateCodeRepository = Mockito.mock(OrderProductStateCodeRepository.class);
        orderProductStateCodeService = new OrderProductStateCodeServiceImpl(orderProductStateCodeRepository);
        orderProductStateCode =
                new OrderProductStateCode(1,
                        OrderProductState.COMPLETE_PAYMENT.getName(),
                        OrderProductState.COMPLETE_PAYMENT.isUsed(),
                        "test");

        requestDto = new CreateOrderProductStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto, "codeName", orderProductStateCode.getCodeName());
        ReflectionTestUtils.setField(requestDto, "codeUsed", orderProductStateCode.isCodeUsed());
        ReflectionTestUtils.setField(requestDto, "codeInfo", orderProductStateCode.getCodeInfo());

        responseDto =
                new GetOrderProductStateCodeResponseDto(
                        orderProductStateCode.getCodeNo(),
                        orderProductStateCode.getCodeName(),
                        orderProductStateCode.isCodeUsed(),
                        orderProductStateCode.getCodeInfo());
    }

    @Test
    void createOrderProductStateCode() {
        orderProductStateCodeService.createOrderProductStateCode(requestDto);

        verify(orderProductStateCodeRepository, times(1)).save(any());
    }

    @Test
    void modifyUsedOrderProductStateCode() {
        when(orderProductStateCodeRepository.findById(orderProductStateCode.getCodeNo()))
                .thenReturn(Optional.of(orderProductStateCode));

        orderProductStateCodeService.modifyUsedOrderProductStateCode(orderProductStateCode.getCodeNo(), false);

        verify(orderProductStateCodeRepository, times(1)).save(any());
    }

    @Test
    void getOrderProductStateCode() {
        when(orderProductStateCodeRepository.findCodeById(orderProductStateCode.getCodeNo()))
                .thenReturn(Optional.of(responseDto));

        assertThat(orderProductStateCodeService.getOrderProductStateCode(orderProductStateCode.getCodeNo())
                .getCodeNo())
                .isEqualTo(orderProductStateCode.getCodeNo());
        assertThat(orderProductStateCodeService.getOrderProductStateCode(orderProductStateCode.getCodeNo())
                .isCodeUsed())
                .isEqualTo(orderProductStateCode.isCodeUsed());
        assertThat(orderProductStateCodeService.getOrderProductStateCode(orderProductStateCode.getCodeNo())
                .getCodeInfo())
                .isEqualTo(orderProductStateCode.getCodeInfo());
        assertThat(orderProductStateCodeService.getOrderProductStateCode(orderProductStateCode.getCodeNo())
                .getCodeName())
                .isEqualTo(orderProductStateCode.getCodeName());
    }

    @Test
    void getOrderProductStateCodes() {
        when(orderProductStateCodeRepository.findCodeAll())
                .thenReturn(List.of(responseDto));

        assertThat(orderProductStateCodeService.getOrderProductStateCodes()
                .get(0).getCodeName())
                .isEqualTo(orderProductStateCode.getCodeName());
        assertThat(orderProductStateCodeService.getOrderProductStateCodes()
                .get(0).getCodeNo())
                .isEqualTo(orderProductStateCode.getCodeNo());
        assertThat(orderProductStateCodeService.getOrderProductStateCodes()
                .get(0).getCodeInfo())
                .isEqualTo(orderProductStateCode.getCodeInfo());
        assertThat(orderProductStateCodeService.getOrderProductStateCodes()
                .get(0).isCodeUsed())
                .isEqualTo(orderProductStateCode.isCodeUsed());
    }
}