package com.nhnacademy.bookpubshop.payment.service;

/**
 * 결제 서비스 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface PaymentService {
    Boolean verifyPayment(String orderId, Long amount);

    void createPayment(String paymentKey, String orderId, Long amount);
}
