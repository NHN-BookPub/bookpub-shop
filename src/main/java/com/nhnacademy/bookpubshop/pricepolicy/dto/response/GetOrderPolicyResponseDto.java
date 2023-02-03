package com.nhnacademy.bookpubshop.pricepolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 오더 view를 구성하는데 필요한 정책들을 반환해주는 dto입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetOrderPolicyResponseDto {
    private Integer policyNo;
    private String policyName;
    private Long policyFee;
}
