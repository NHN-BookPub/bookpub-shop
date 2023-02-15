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

import com.nhnacademy.bookpubshop.coupon.dummy.CouponDummy;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.relationship.dummy.OrderProductDummy;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductException;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductStateException;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.exception.NotFoundOrderStateException;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.payment.adaptor.TossAdaptor;
import com.nhnacademy.bookpubshop.payment.dto.request.OrderProductRefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.response.GetRefundResponseDto;
import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;
import com.nhnacademy.bookpubshop.payment.dummy.PaymentDummy;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.exception.NotFoundPaymentException;
import com.nhnacademy.bookpubshop.payment.repository.PaymentRepository;
import com.nhnacademy.bookpubshop.payment.service.PaymentService;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.exception.NotFoundPaymentStateException;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepository;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepository;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;

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
    @MockBean
    OrderProductStateCodeRepository orderProductStateCodeRepository;
    @MockBean
    OrderProductRepository orderProductRepository;
    @MockBean
    CouponRepository couponRepository;
    @MockBean
    OrderStateCodeRepository orderStateCodeRepository;
    @MockBean
    ProductRepository productRepository;

    GetOrderVerifyResponseDto getOrderVerifyResponseDto;
    TossResponseDto tossResponseDto;
    BookpubOrder order;
    Member member;
    PricePolicy deliveryPricePolicy;
    PricePolicy packagePricePolicy;
    OrderStateCode orderStateCode;
    OrderProductStateCode orderProductStateCode;

    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    PaymentStateCode paymentStateCode;
    PaymentTypeStateCode paymentTypeState;
    Payment payment;
    RefundRequestDto refundRequestDto;
    OrderProduct orderProduct;
    GetRefundResponseDto refundResponseDto;
    OrderProductRefundRequestDto orderProductRefundRequestDto;
    Product product;
    Coupon coupon;

    @BeforeEach
    void setUp() {

        orderProductRefundRequestDto = new OrderProductRefundRequestDto(1L, 1L, "cancel");
        refundResponseDto = new GetRefundResponseDto("payment");
        refundRequestDto = new RefundRequestDto();
        ReflectionTestUtils.setField(refundRequestDto, "orderNo", 1L);
        ReflectionTestUtils.setField(refundRequestDto, "cancelReason", "cancel");

        orderProductStateCode = new OrderProductStateCode(1, "name", true, "info");
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
        payment = PaymentDummy.dummy(order, paymentStateCode, paymentTypeState);

        order = OrderDummy.dummy2(member, deliveryPricePolicy, packagePricePolicy, orderStateCode);

        tossResponseDto.setApprovedAt("2022-09-08T12:09:00+09:00");
        tossResponseDto.setPaymentKey("paymentKey");
        TossResponseDto.Receipt receipt = new TossResponseDto.Receipt();
        receipt.setUrl("213123");
        tossResponseDto.setReceipt(receipt);
        coupon = CouponDummy.dummy(null, order, orderProduct,member);
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        orderProduct = OrderProductDummy.dummy(product,order,orderProductStateCode);

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

        paymentService.createPayment(anyString(), anyString(), anyLong());

        verify(paymentRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("주문환불 결제정보 찾지못할경우")
    void refundOrderFail() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(NotFoundPaymentException.class)
                .hasMessageContaining(NotFoundPaymentException.MESSAGE);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
    }

    @Test
    @DisplayName("주문환불 결제정보 찾지못할경우")
    void refundOrderPaymentFail() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(NotFoundPaymentException.class)
                .hasMessageContaining(NotFoundPaymentException.MESSAGE);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
    }

    @Test
    @DisplayName("주문환불 statecode 를 찾지못한경우")
    void refundOrderStateFail() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(NotFoundPaymentStateException.class)
                .hasMessageContaining(NotFoundPaymentStateException.MESSAGE);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
        verify(paymentStateCodeRepository, times(1)).getPaymentStateCode(anyString());
    }

    @Test
    @DisplayName("주문환불 paymentkey 로 값을 찾지못할경우")
    void refundOrderPaymentKeyException() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining(OrderNotFoundException.MESSAGE);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
        verify(paymentStateCodeRepository, times(1)).getPaymentStateCode(anyString());
        verify(paymentRepository, times(1)).getOrderByPaymentKey(any());
    }

    @Test
    @DisplayName("주문환불 결제정보 찾지못할경우")
    void refundOrderCodeException() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(NotFoundOrderStateException.class)
                .hasMessageContaining(NotFoundOrderStateException.MESSAGE);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
        verify(paymentStateCodeRepository, times(1)).getPaymentStateCode(anyString());
        verify(paymentRepository, times(1)).getOrderByPaymentKey(any());
        verify(orderStateCodeRepository, times(1)).findByCodeName(anyString());
    }

    @Test
    @DisplayName("주문환불 code 값으로 값을 찾지못할경우")
    void refundOrderProductStateException() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(NotFoundOrderProductStateException.class)
                .hasMessageContaining(NotFoundOrderProductStateException.MESSAGE);


        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
        verify(paymentStateCodeRepository, times(1)).getPaymentStateCode(anyString());
        verify(paymentRepository, times(1)).getOrderByPaymentKey(any());
        verify(orderStateCodeRepository, times(1)).findByCodeName(anyString());
        verify(orderProductStateCodeRepository, times(1)).findByCodeName(anyString());
    }

    @Test
    @DisplayName("주문환불 상품정보를 찾지못할경우")
    void refundOrderProductNotFoundException() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(orderRepository.getOrderProductList(anyLong()))
                .thenReturn(List.of(orderProduct));
        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrder(refundRequestDto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
        verify(paymentStateCodeRepository, times(1)).getPaymentStateCode(anyString());
        verify(paymentRepository, times(1)).getOrderByPaymentKey(any());
        verify(orderStateCodeRepository, times(1)).findByCodeName(anyString());
        verify(orderProductStateCodeRepository, times(1)).findByCodeName(anyString());
        verify(productRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("주문환불 결제정보 찾지못할경우")
    void refundSuccess() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(tossAdaptor.requestRefund(anyString(), anyString()))
                .thenReturn(tossResponseDto);
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(orderRepository.getOrderProductList(anyLong()))
                .thenReturn(List.of(orderProduct));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(couponRepository.findByCouponByOrderNo(anyLong()))
                .thenReturn(List.of(coupon));

        paymentService.refundOrder(refundRequestDto);

        verify(paymentRepository, times(1)).getRefundInfo(refundRequestDto);
        verify(tossAdaptor, times(1)).requestRefund(anyString(), anyString());
        verify(paymentRepository, times(1)).getPayment(any());
        verify(paymentStateCodeRepository, times(1)).getPaymentStateCode(anyString());
        verify(paymentRepository, times(1)).getOrderByPaymentKey(any());
        verify(orderStateCodeRepository, times(1)).findByCodeName(anyString());
        verify(orderProductStateCodeRepository, times(1)).findByCodeName(anyString());
        verify(productRepository, times(1)).findById(any());
        verify(couponRepository, times(1)).findByCouponByOrderNo(anyLong());
    }

    @Test
    @DisplayName("주문상품 결제취소 주문상품을 찾지못할경우")
    void refundOrderProductNotOrderProductException() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentTypeRepository.getPaymentType(anyString()))
                .thenReturn(Optional.of(paymentTypeState));
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(tossAdaptor.requestRefund(anyString(),anyString()))
                .thenReturn(tossResponseDto);
        when(orderProductRepository.getOrderProduct(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrderProduct(orderProductRefundRequestDto))
                .isInstanceOf(NotFoundOrderProductException.class)
                .hasMessageContaining(NotFoundOrderProductException.MESSAGE);

        verify(orderProductRepository, times(1))
                .getOrderProductList(anyLong());
        verify(orderProductRepository, times(1))
                .getOrderProduct(anyLong());
    }

    @Test
    @DisplayName("주문상품 상품을 찾지못할경우")
    void refundOrderProductNotStatetException() {
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentTypeRepository.getPaymentType(anyString()))
                .thenReturn(Optional.of(paymentTypeState));
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(tossAdaptor.requestRefund(anyString(),anyString()))
                .thenReturn(tossResponseDto);
        when(orderProductRepository.getOrderProduct(anyLong()))
                .thenReturn(Optional.of(orderProduct));

        assertThatThrownBy(() -> paymentService.refundOrderProduct(orderProductRefundRequestDto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);

        verify(orderProductRepository, times(1))
                .getOrderProductList(anyLong());
        verify(orderProductRepository, times(1))
                .getOrderProduct(anyLong());
        verify(productRepository, times(1))
                .findById(any());
    }

    @Test
    @DisplayName("주문상품 결제취소 상태를 찾지못할경우")
    void refundOrderProductStateFail() {
        when(orderProductRepository.getOrderProductList(anyLong()))
                .thenReturn(List.of(orderProduct));
        when(orderProductRepository.getOrderProduct(anyLong()))
                .thenReturn(Optional.of(orderProduct));
        when(couponRepository.findByCouponByOrderProductNo(anyLong()))
                .thenReturn(List.of(coupon));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refundOrderProduct(orderProductRefundRequestDto))
                .isInstanceOf(NotFoundPaymentException.class)
                .hasMessageContaining(NotFoundPaymentException.MESSAGE);

        verify(orderProductRepository, times(1))
                .getOrderProductList(anyLong());

    }

    @Test
    @DisplayName("주문상품 환불 성공")
    void refundOrderProductSuccess() {
        when(orderProductRepository.getOrderProductList(anyLong()))
                .thenReturn(List.of(orderProduct));
        when(orderProductRepository.getOrderProduct(anyLong()))
                .thenReturn(Optional.of(orderProduct));
        when(couponRepository.findByCouponByOrderProductNo(anyLong()))
                .thenReturn(List.of(coupon));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(paymentStateCodeRepository.getPaymentStateCode(anyString()))
                .thenReturn(Optional.of(paymentStateCode));
        when(paymentTypeRepository.getPaymentType(anyString()))
                .thenReturn(Optional.of(paymentTypeState));
        when(paymentRepository.getRefundInfo(any()))
                .thenReturn(Optional.of(refundResponseDto));
        when(paymentRepository.getPayment(anyString()))
                .thenReturn(Optional.of(payment));
        when(paymentRepository.getOrderByPaymentKey(anyString()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(tossAdaptor.requestRefund(anyString(),anyString()))
                .thenReturn(tossResponseDto);

        paymentService.refundOrderProduct(orderProductRefundRequestDto);

        verify(orderProductRepository, times(1))
                .getOrderProductList(anyLong());
        verify(orderProductStateCodeRepository, times(1))
                .findByCodeName(anyString());
        verify(orderProductRepository, times(0))
                .getOrderProduct(anyLong());
    }
}