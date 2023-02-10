package com.nhnacademy.bookpubshop.paymentstatecode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제상태 반환 dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPaymentStateResponseDto {
    private Integer codeNo;
    private String codeName;
    private boolean codeUsed;
    private String codeInfo;
}
