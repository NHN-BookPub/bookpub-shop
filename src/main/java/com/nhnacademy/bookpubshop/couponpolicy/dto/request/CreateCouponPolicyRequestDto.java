package com.nhnacademy.bookpubshop.couponpolicy.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰정책 등록을 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateCouponPolicyRequestDto {

    private boolean policyFixed;

    @NotNull(message = "값을 기입하여야 합니다.")
    @PositiveOrZero(message = "0 이상의 값을 기입하여야 합니다.")
    private Long policyPrice;

    @NotNull(message = "값을 기입하여야 합니다.")
    @PositiveOrZero(message = "0 이상의 값을 기입하여야 합니다.")
    private Long policyMinimum;

    @PositiveOrZero(message = "0 이상의 값을 기입하여야 합니다.")
    private Long maxDiscount;
}
