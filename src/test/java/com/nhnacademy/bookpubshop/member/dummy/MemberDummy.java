package com.nhnacademy.bookpubshop.member.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;

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
}
