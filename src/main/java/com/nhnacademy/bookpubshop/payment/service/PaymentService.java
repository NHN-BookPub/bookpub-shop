package com.nhnacademy.bookpubshop.payment.service;

import com.nhnacademy.bookpubshop.payment.dto.request.OrderProductRefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;

/**
 * 결제 서비스 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface PaymentService {
    /**
     * 유효한 주문인지 검증하는 메소드.
     *
     * @param orderId 주문 아이디.
     * @param amount 주문 금액.
     * @return 유효한지 아닌지.
     */
    Boolean verifyPayment(String orderId, Long amount);

    /**
     * 결제 생성하는 메소드.
     *
     * @param paymentKey 결제 키.
     * @param orderId 주문 아이디.
     * @param amount 주문 금액.
     */
    void createPayment(String paymentKey, String orderId, Long amount);

    /**
     * 주문 결제 취소하는 메소드.
     *
     * @param refundRequestDto 환불 dto.
     */
    void refundOrder(RefundRequestDto refundRequestDto);

    /**
     * 주문상품 결제 취소하는 메소드.
     *
     * @param refundRequestDto 환불 dto.
     */
    void refundOrderProduct(OrderProductRefundRequestDto refundRequestDto);

    /**
     * 주문상품 교환 신청하는 메소드.
     *
     * @param exchangeRequestDto 교환신청 dto.
     */
    void exchangeOrderProduct(OrderProductRefundRequestDto exchangeRequestDto);
}
