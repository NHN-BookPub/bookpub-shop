package com.nhnacademy.bookpubshop.pricepolicy.dto.request;

import com.nhnacademy.bookpubshop.state.PricePolicyState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 가격 정책 생성 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreatePricePolicyRequestDto {
    @StateCode(enumClass = PricePolicyState.class)
    private String policyName;
    @NotNull
    private Long policyFee;
}