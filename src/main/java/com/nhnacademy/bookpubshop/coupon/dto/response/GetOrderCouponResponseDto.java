package com.nhnacademy.bookpubshop.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문에 필요한 쿠폰 정보를 담은 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetOrderCouponResponseDto {
    private Long couponNo;
    private String templateName;
    private Long productNo;
    private Integer categoryNo;
    private boolean policyFixed;
    private Long policyPrice;
    private Long policyMinimum;
    private Long maxDiscount;
    private boolean templateBundled;
}
