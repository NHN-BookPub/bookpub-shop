package com.nhnacademy.bookpubshop.payment.adaptor;

import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;

/**
 * 토스와 통신하는 adaptor.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface TossAdaptor {
    /**
     * 토스 서버에 결제요청하는 메소드.
     *
     * @param paymentKey 결제 키.
     * @param orderId 주문 아이디.
     * @param amount 총 금액.
     * @return 결제응답.
     */
    TossResponseDto requestPayment(String paymentKey, String orderId, Long amount);

    /**
     * 토스 서버에 결제 취소 요청하는 메소드.
     *
     * @param paymentKey 결제 키.
     * @param cancelReason 취소 사유.
     * @return 취소응답.
     */
    TossResponseDto requestRefund(String paymentKey, String cancelReason);
}
