package com.nhnacademy.bookpubshop.product.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품정책 조회시 반환하는 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductPolicyResponseDto {
    private Integer policyNo;
    private String policyMethod;
    private boolean policySaved;
    private Integer saveRate;
}
