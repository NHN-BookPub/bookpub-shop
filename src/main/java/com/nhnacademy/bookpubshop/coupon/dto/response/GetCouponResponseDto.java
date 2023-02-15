package com.nhnacademy.bookpubshop.coupon.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 쿠폰 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetCouponResponseDto {
    private Long couponNo;
    private String memberId;
    private String templateName;
    private String templateImage;
    private String typeName;
    private boolean policyFixed;
    private Long policyPrice;
    private Long policyMinimum;
    private Long maxDiscount;
    private LocalDateTime finishedAt;
    private boolean couponUsed;
}
