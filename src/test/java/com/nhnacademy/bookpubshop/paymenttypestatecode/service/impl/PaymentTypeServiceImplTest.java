package com.nhnacademy.bookpubshop.paymenttypestatecode.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepository;
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
@Import(PaymentTypeServiceImpl.class)
@ExtendWith(SpringExtension.class)
class PaymentTypeServiceImplTest {
    @Autowired
    PaymentTypeServiceImpl paymentTypeService;

    @MockBean
    PaymentTypeRepository paymentTypeRepository;

    GetPaymentTypeResponseDto response;
    PaymentTypeStateCode paymentTypeStateCode;

    @BeforeEach
    void setUp() {
        response = new GetPaymentTypeResponseDto(1, "name", true, "dlfma");
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
    }

    @Test
    @DisplayName("모든 결제유형을 반환.")
    void getAllPaymentType() {
        Mockito.when(paymentTypeRepository.getAllPaymentType()).thenReturn(
                List.of(response)
        );

        List<GetPaymentTypeResponseDto> allPaymentType = paymentTypeService.getAllPaymentType();
        assertThat(allPaymentType).hasSize(1);
        assertThat(allPaymentType.get(0)).isEqualTo(response);
    }

    @Test
    @DisplayName("타입으로 결제 타입 객체를 반환")
    void getPaymentType() {
        Mockito.when(paymentTypeRepository.getPaymentType(anyString()))
                .thenReturn(Optional.of(paymentTypeStateCode));

        PaymentTypeStateCode paymentType = paymentTypeService.getPaymentType(anyString());

        assertThat(paymentType).isEqualTo(paymentTypeStateCode);
    }
}