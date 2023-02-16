package com.nhnacademy.bookpubshop.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 매출관련 DTO 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class TotalSaleDto {
    private Long cancelPaymentCnt;
    private Long cancelPaymentAmount;
    private Long cancelOrderCnt;
    private Long saleCnt;
    private Long saleAmount;
    private Long total;
}
