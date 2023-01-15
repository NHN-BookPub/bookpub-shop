package com.nhnacademy.bookpubshop.coupontemplate.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰템플릿 자세한 정보 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailCouponTemplateResponseDto {
    private Long templateNo;
    private boolean policyFixed;
    private Long pricePrice;
    private Long policyMinimum;
    private Long maxDiscount;
    private String typeName;
    private String productTitle;
    private String categoryName;
    private String codeTarget;
    private String templateName;
    private String templateImage;
    private LocalDateTime finishedAt;
    private LocalDateTime issuedAt;
    private boolean templateOverlapped;
    private boolean templateBundled;
}
