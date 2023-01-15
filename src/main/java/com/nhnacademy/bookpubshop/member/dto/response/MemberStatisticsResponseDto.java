package com.nhnacademy.bookpubshop.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 멤버의 통계를 보여주는 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberStatisticsResponseDto {
    private Long memberCnt;

    private Long currentMemberCnt;

    private Long deleteMemberCnt;

    private Long blockMemberCnt;
}
