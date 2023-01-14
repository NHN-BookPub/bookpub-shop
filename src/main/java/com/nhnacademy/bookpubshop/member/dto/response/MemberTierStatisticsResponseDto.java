package com.nhnacademy.bookpubshop.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 멤버별 등급 통계를 반환해주는 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberTierStatisticsResponseDto {
    private String tierName;
    private Integer tierValue;

    private Long tierCnt;
}
