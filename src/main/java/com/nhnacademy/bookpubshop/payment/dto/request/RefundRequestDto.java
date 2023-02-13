package com.nhnacademy.bookpubshop.payment.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 취소사유가 들어있는 dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDto {
    @NotNull(message = "주문번호는 null 이 될수없습니다.")
    private Long orderNo;

    @NotNull(message = "cancelReason 은 null이 될수없습니다.")
    @Size(min = 1, max = 200, message = "사이즈는 1부터 200 안에 되어야합니다.")
    private String cancelReason;
}
