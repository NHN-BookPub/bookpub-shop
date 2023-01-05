package com.nhnacademy.bookpubshop.member.dummy;

import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import java.time.LocalDateTime;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class MemberDummy {
    public static Member dummy() {
        return new Member(null,
                TierDummy.dummy(),
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
