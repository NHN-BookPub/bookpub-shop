package com.nhnacademy.bookpubshop.order.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 교환신청한 주문상품의 정보를 얻어오는 dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetExchangeResponseDto {
    private Long orderProductNo;
    private String memberId;
    private Long productNo;
    private String title;
    private String thumbnail;
    private Integer productAmount;
    private String stateCode;
    private String exchangeReason;
}
