package com.nhnacademy.bookpubshop.coupon.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 수정을 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyCouponRequestDto {
    @NotNull(message = "쿠폰 번호를 입력해주세요.")
    private Long couponNo;
    private boolean couponUsed;
}
