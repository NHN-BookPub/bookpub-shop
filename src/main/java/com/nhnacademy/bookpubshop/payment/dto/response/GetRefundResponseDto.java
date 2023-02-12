package com.nhnacademy.bookpubshop.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 환불을 하기 위해 필요한 정보가 담겨있는 dto입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetRefundResponseDto {
    private String paymentKey;
}
