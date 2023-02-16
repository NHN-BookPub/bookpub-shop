package com.nhnacademy.bookpubshop.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 시간당 주문수를 측정하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class OrderCntResponseDto {
    private Integer date;
    private Long orderCnt;
}
