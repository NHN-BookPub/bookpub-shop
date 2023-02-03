package com.nhnacademy.bookpubshop.pricepolicy.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 가격정책을 반환하기 위한 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPricePolicyResponseDto {
    private Integer policyNo;
    private String policyName;
    private Long policyFee;
    private LocalDateTime createdAt;
}
