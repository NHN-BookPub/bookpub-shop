package com.nhnacademy.bookpubshop.payment.adaptor;

import com.nhnacademy.bookpubshop.payment.dto.TossResponseDto;

/**
 * 토스와 통신하는 adaptor.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface TossAdaptor {
    TossResponseDto requestPayment(String paymentKey, String orderId, Long amount);
}
