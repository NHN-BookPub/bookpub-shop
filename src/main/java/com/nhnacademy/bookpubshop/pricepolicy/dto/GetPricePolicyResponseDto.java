package com.nhnacademy.bookpubshop.pricepolicy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPricePolicyResponseDto {
    private Integer pricePolicyNo;
    private String policyName;
    private Long policyFee;
}
