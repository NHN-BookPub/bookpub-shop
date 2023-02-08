package com.nhnacademy.bookpubshop.paymentstatecode.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepository;
import com.nhnacademy.bookpubshop.paymentstatecode.service.PaymentStateService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 결제타입 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Import(PaymentStateServiceImpl.class)
@ExtendWith(SpringExtension.class)
class PaymentStateServiceImplTest {
    @Autowired
    PaymentStateService paymentTypeService;

    @MockBean
    PaymentStateCodeRepository paymentStateCodeRepository;

    GetPaymentStateResponseDto response;
    PaymentStateCode paymentStateCode;

    @BeforeEach
    void setUp() {
        response = new GetPaymentStateResponseDto(1, "name", true, "dlfma");
        paymentStateCode = PaymentStateCodeDummy.dummy();
    }

    @Test
    @DisplayName("모든 결제유형을 반환.")
    void getAllPaymentType() {
        Mockito.when(paymentStateCodeRepository.getAllPaymentState()).thenReturn(
                List.of(response)
        );

        List<GetPaymentStateResponseDto> allPaymentType = paymentTypeService.getAllPaymentState();
        assertThat(allPaymentType).hasSize(1);
        assertThat(allPaymentType.get(0)).isEqualTo(response);
    }

    @Test
    @DisplayName("타입으로 결제 타입 객체를 반환")
    void getPaymentType() {
        Mockito.when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));

        PaymentStateCode paymentState = paymentTypeService.getPaymentState(anyString());

        assertThat(paymentState).isEqualTo(paymentStateCode);
    }
}