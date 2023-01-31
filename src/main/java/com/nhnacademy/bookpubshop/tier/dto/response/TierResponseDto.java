package com.nhnacademy.bookpubshop.tier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등급에대한 기본정보를 반환하기위한 DTO 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TierResponseDto {
    private Integer tierNo;
    private String tierName;

    private Integer tierValue;

    private Long tierPrice;

    private Long tierPoint;

}
