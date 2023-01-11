package com.nhnacademy.bookpubshop.coupontype.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰유형 이름 조회를 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponTypeResponseDto {
    private Long typeNo;
    private String typeName;
}
