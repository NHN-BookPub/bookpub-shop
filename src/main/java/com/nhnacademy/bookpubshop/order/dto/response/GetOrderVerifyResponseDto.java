package com.nhnacademy.bookpubshop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 생성시 같은 주문인지 검증하는 response dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetOrderVerifyResponseDto {
    private Long amount;
}
