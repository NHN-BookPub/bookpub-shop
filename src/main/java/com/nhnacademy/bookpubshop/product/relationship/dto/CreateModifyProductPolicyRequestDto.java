package com.nhnacademy.bookpubshop.product.relationship.dto;

import javax.validation.constraints.NotNull;
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
    @Length(max = 10)
    private String policyMethod;
    @NotNull
    private boolean policySaved;
    @NotNull
    private Integer saveRate;
}
