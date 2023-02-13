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
    @NotNull(message = "주문번호는 null 될수 없습니다.")
    private Long orderNo;

    @NotNull(message = "주문상품번호는 null 이 될 수 없습니다.")
    private Long orderProductNo;

    @NotNull(message = "취소사유는 null 이 될 수 없습니다.")
    @Size(min = 1, max = 200, message = "취소이유는 1~200 자 내외여야 합니다.")
    private String cancelReason;
}
