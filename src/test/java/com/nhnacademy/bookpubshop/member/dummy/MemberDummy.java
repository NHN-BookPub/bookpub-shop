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
        return new Member(null,
                bookPubTier,
                "id",
                "nickname",
                "taewon",
                "남성",
                1234,
                1231,
                "!@!#@ASD",
                "12345678",
                "email@email.com",
                LocalDateTime.now(),
                false,
                false,
                null,
                0L,
                false);
    }
}
