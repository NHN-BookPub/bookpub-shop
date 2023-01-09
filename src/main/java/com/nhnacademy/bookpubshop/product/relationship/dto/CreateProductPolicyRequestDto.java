package com.nhnacademy.bookpubshop.product.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

/**
 * 상품정책생성시 받을 Dto class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateProductPolicyRequestDto {
    private String policyMethod;
    private boolean policySaved;
    @Length(max = 100)
    private Integer saveRate;
}
