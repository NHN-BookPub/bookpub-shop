package com.nhnacademy.bookpubshop.tier.dto.request;

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
public class TierCreateRequestDto {
    private String tierName;
}
