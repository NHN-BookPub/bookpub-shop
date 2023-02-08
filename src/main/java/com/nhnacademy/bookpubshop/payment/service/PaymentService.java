package com.nhnacademy.bookpubshop.payment.service;

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
}
