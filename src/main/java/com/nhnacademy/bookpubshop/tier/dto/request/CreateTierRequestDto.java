package com.nhnacademy.bookpubshop.tier.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등급을 생성하기위한 Dto 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@NoArgsConstructor
@Getter
public class CreateTierRequestDto {
    @NotBlank(message = "등급의 이름을 기입하여야 합니다.")
    private String tierName;
}
