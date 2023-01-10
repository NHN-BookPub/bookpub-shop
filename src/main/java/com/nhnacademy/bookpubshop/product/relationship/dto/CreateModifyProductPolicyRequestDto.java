package com.nhnacademy.bookpubshop.product.relationship.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 상품정책생성시 받을 Dto class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateModifyProductPolicyRequestDto {
    private String policyMethod;
    private boolean policySaved;
    @Length(max = 100)
    private Integer saveRate;
}
