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
    @Length(max = 10, message = "10글자를 넘을 수 없습니다.")
    private String policyMethod;
    @NotNull
    private boolean policySaved;
    private Integer saveRate;
}
