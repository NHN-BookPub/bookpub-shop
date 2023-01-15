package com.nhnacademy.bookpubshop.couponmonth.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이달의쿠폰 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetCouponMonthResponseDto {
    private Long monthNo;
    private Long templateNo;
    private String templateName;
    private String templateImage;
    private LocalDateTime openedAt;
    private Integer monthQuantity;
}
