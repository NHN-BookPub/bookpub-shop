package com.nhnacademy.bookpubshop.couponstatecode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰상태코드 조회를 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponStateCodeResponseDto {
    private Integer codeNo;
    private String codeTarget;
}
