package com.nhnacademy.bookpubshop.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetOrderCouponResponseDto {
    private Long couponNo;
    private String templateName;
    private boolean policyFixed;
    private Long policyPrice;
    private Long policyMinimum;
    private Long maxDiscount;
    private boolean templateBundled;
}
