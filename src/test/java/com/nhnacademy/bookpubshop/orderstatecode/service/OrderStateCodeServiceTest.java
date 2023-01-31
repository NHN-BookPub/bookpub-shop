package com.nhnacademy.bookpubshop.orderstatecode.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.orderstatecode.dto.CreateOrderStateCodeRequestDto;
import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.orderstatecode.service.impl.OrderStateCodeServiceImpl;
import com.nhnacademy.bookpubshop.state.OrderState;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 주문상태코드 서비스 테스트입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class OrderStateCodeServiceTest {
    OrderStateCode orderStateCode;
    OrderStateCodeRepository orderStateCodeRepository;
    OrderStateCodeService orderStateCodeService;
    CreateOrderStateCodeRequestDto requestDto;
    GetOrderStateCodeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        orderStateCode = new OrderStateCode(1,
                OrderState.COMPLETE_DELIVERY.getName(),
                OrderState.CANCEL_DELIVERY.isUsed(),
                "test");

        orderStateCodeRepository = Mockito.mock(OrderStateCodeRepository.class);
        orderStateCodeService = new OrderStateCodeServiceImpl(orderStateCodeRepository);
        requestDto = new CreateOrderStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto, "codeName", orderStateCode.getCodeName());
        ReflectionTestUtils.setField(requestDto, "codeUsed", orderStateCode.isCodeUsed());
        ReflectionTestUtils.setField(requestDto, "codeInfo", orderStateCode.getCodeInfo());


        responseDto = new GetOrderStateCodeResponseDto(
                orderStateCode.getCodeNo(),
                orderStateCode.getCodeName(),
                orderStateCode.isCodeUsed(),
                orderStateCode.getCodeInfo());
    }

    @Test
    void createPricePolicy() {
        when(orderStateCodeRepository.save(orderStateCode))
                .thenReturn(orderStateCode);

        orderStateCodeService.createPricePolicy(requestDto);

        verify(orderStateCodeRepository, times(1)).save(any());
    }

    @Test
    void getOrderStateCodeById() {
        when(orderStateCodeRepository.findStateCodeByNo(orderStateCode.getCodeNo()))
                .thenReturn(Optional.of(responseDto));

        assertThat(orderStateCodeService.getOrderStateCodeById(orderStateCode.getCodeNo())
                .getCodeNo())
                .isEqualTo(responseDto.getCodeNo());
        assertThat(orderStateCodeService.getOrderStateCodeById(orderStateCode.getCodeNo())
                .isCodeUsed())
                .isEqualTo(responseDto.isCodeUsed());
        assertThat(orderStateCodeService.getOrderStateCodeById(orderStateCode.getCodeNo())
                .getCodeName())
                .isEqualTo(responseDto.getCodeName());
        assertThat(orderStateCodeService.getOrderStateCodeById(orderStateCode.getCodeNo())
                .getCodeInfo())
                .isEqualTo(responseDto.getCodeInfo());
    }

    @Test
    void getOrderStateCodes() {
        when(orderStateCodeRepository.findStateCodes())
                .thenReturn(List.of(responseDto));

        assertThat(orderStateCodeService.getOrderStateCodes().get(0)
                .getCodeNo())
                .isEqualTo(responseDto.getCodeNo());
        assertThat(orderStateCodeService.getOrderStateCodes().get(0)
                .isCodeUsed())
                .isEqualTo(responseDto.isCodeUsed());
        assertThat(orderStateCodeService.getOrderStateCodes().get(0)
                .getCodeName())
                .isEqualTo(responseDto.getCodeName());
        assertThat(orderStateCodeService.getOrderStateCodes().get(0)
                .getCodeInfo())
                .isEqualTo(responseDto.getCodeInfo());
    }

    @Test
    void modifyOrderStateCodeUsed() {
        when(orderStateCodeRepository.findById(orderStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(orderStateCode));

        orderStateCodeService.modifyOrderStateCodeUsed(orderStateCode.getCodeNo());

        verify(orderStateCodeRepository, times(1))
                .save(any());
    }
}