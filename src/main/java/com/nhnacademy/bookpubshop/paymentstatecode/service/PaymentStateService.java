package com.nhnacademy.bookpubshop.paymentstatecode.service;

import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import java.util.List;

/**
 * 결제상태 서비스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface PaymentStateService {
    /**
     * 모든 결제상태를 불러오는 메소드.
     *
     * @return 모든 결제상태.
     */
    List<GetPaymentStateResponseDto> getAllPaymentState();

    /**
     * 상태 이름으로 결제상태를 검색해오는 메소드.
     *
     * @param state 상태명.
     * @return 결제상태 정보..
     */
    PaymentStateCode getPaymentState(String state);
}
