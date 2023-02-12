package com.nhnacademy.bookpubshop.payment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.payment.adaptor.TossAdaptor;
import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;
import com.nhnacademy.bookpubshop.payment.dummy.PaymentDummy;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.repository.PaymentRepository;
import com.nhnacademy.bookpubshop.payment.service.PaymentService;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepository;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepository;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 결제 서비스 테스트입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(PaymentServiceImpl.class)
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;

    @MockBean
    PaymentRepository paymentRepository;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    PaymentStateCodeRepository paymentStateCodeRepository;
    @MockBean
    PaymentTypeRepository paymentTypeRepository;
    @MockBean
    TossAdaptor tossAdaptor;
    @MockBean
    ApplicationEventPublisher applicationEventPublisher;

    GetOrderVerifyResponseDto getOrderVerifyResponseDto;
    TossResponseDto tossResponseDto;
    BookpubOrder order;
    Member member;
    PricePolicy deliveryPricePolicy;
    PricePolicy packagePricePolicy;
    OrderStateCode orderStateCode;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    PaymentStateCode paymentStateCode;
    PaymentTypeStateCode paymentTypeState;
    Payment payment;

    @BeforeEach
    void setUp() {
        getOrderVerifyResponseDto = new GetOrderVerifyResponseDto(15000L);
        tossResponseDto = new TossResponseDto();
        member = MemberDummy.dummy(TierDummy.dummy());
        deliveryPricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = PricePolicyDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        paymentStateCode = PaymentStateCodeDummy.dummy();
        paymentTypeState = PaymentTypeStateCodeDummy.dummy();
        payment = PaymentDummy.dummy(order,paymentStateCode,paymentTypeState);

        order = OrderDummy.dummy2(member, deliveryPricePolicy, packagePricePolicy, orderStateCode);

        tossResponseDto.setApprovedAt("2022-09-08T12:09:00+09:00");
        tossResponseDto.setPaymentKey("paymentKey");
        TossResponseDto.Receipt receipt = new TossResponseDto.Receipt();
        receipt.setUrl("213123");
        tossResponseDto.setReceipt(receipt);

    }

    @Test
    @DisplayName("검증 된 결제입니다.")
    void verifyPayment() {
        when(orderRepository.verifyPayment(anyString())).thenReturn(Optional.of(getOrderVerifyResponseDto));

        Boolean verifyPayment = paymentService.verifyPayment("orderId", 15000L);

        assertThat(verifyPayment).isTrue();

        verify(orderRepository, times(1))
                .verifyPayment(anyString());
    }

    @Test
    @DisplayName("검증 되지 않은 결제입니다.")
    void verifyPayment_fail() {
        when(orderRepository.verifyPayment(anyString())).thenReturn(Optional.of(getOrderVerifyResponseDto));

        Boolean verifyPayment = paymentService.verifyPayment("orderId", 20000L);

        assertThat(verifyPayment).isFalse();

        verify(orderRepository, times(1))
                .verifyPayment(anyString());
    }

    @Test
    @DisplayName("주문이 없습니다.")
    void verifyPayment_fail_orderNotFound() {
        when(orderRepository.verifyPayment(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.verifyPayment("orderId", 20000L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining(OrderNotFoundException.MESSAGE);

    }

    @Test
    @DisplayName("결제를 생성합니다.")
    void createPayment() {
        when(tossAdaptor.requestPayment(anyString(), anyString(), anyLong()))
                .thenReturn(tossResponseDto);
        when(orderRepository.getOrderByOrderKey(anyString()))
                .thenReturn(Optional.ofNullable(order));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.ofNullable(paymentStateCode));
        when(paymentTypeRepository.getPaymentType(anyString()))
                .thenReturn(Optional.ofNullable(paymentTypeState));
        when(paymentRepository.save(any())).thenReturn(payment);
        doNothing().when(applicationEventPublisher).publishEvent(any());

        paymentService.createPayment(anyString(),anyString(),anyLong());

        verify(paymentRepository, times(1)).save(any());

    }
}