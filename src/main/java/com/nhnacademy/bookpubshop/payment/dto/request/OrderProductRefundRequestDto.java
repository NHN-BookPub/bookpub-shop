package com.nhnacademy.bookpubshop.payment.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문상품의 환불을 요청하는 dto입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductRefundRequestDto {
    @NotNull
    private Long orderNo;

    @NotNull
    private Long orderProductNo;

    @NotNull
    @Size(min = 1, max = 200)
    private String cancelReason;
}
