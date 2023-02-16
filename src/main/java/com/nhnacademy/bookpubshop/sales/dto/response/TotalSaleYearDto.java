package com.nhnacademy.bookpubshop.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 월별 통계를 얻기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class TotalSaleYearDto {
    private Long cancelPaymentCnt;
    private Long cancelPaymentAmount;
    private Long cancelOrderCnt;
    private Long saleCnt;
    private Long saleAmount;
    private Long total;
    private Integer month;
}
