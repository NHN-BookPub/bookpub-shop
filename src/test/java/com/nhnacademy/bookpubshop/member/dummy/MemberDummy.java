package com.nhnacademy.bookpubshop.member.dummy;

import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.ArrayList;

/**
 * 회원 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class MemberDummy {
    public static Member dummy(BookPubTier bookPubTier) {
        return Member.builder()
                .tier(bookPubTier)
                .memberId("test_id")
                .memberNickname("test_nickname")
                .memberName("test_name")
                .memberGender("남")
                .memberBirthYear(2022)
                .memberBirthMonth(819)
                .memberPwd("test_pwd")
                .memberPhone("010-1111-2222")
                .memberEmail("test@gmail.com")
                .build();
    }

    public static LoginMemberResponseDto dummy2() {
        return new LoginMemberResponseDto(
                3L,
                "tagkdj1",
                "Abc123!@#",
                new ArrayList<>()
        );
    }

    public static Member dummy2(BookPubTier bookPubTier) {
        return Member.builder()
                .tier(bookPubTier)
                .memberId("test_id2")
                .memberNickname("test_nickname2")
                .memberName("test_name")
                .memberGender("남")
                .memberBirthYear(2022)
                .memberBirthMonth(819)
                .memberPwd("test_pwd")
                .memberPhone("010-1111-2222")
                .memberEmail("test@gmail.com")
                .build();
    }

    public static MemberStatisticsResponseDto memberStatisticsDummy(){
        return new MemberStatisticsResponseDto(
                1L,
                1L,
                1L,
                1L
        );
    }

    public static MemberTierStatisticsResponseDto memberTierStatisticsDummy() {
        return new MemberTierStatisticsResponseDto("tiername", 1, 1L);
    }
}
