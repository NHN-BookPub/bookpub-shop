package com.nhnacademy.bookpubshop.coupon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * RabbitMQ 에서 사용하는 이달의 쿠폰 dto 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IssueCouponMonthDto {

    private Long memberNo;
    private Long templateNo;
}
