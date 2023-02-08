package com.nhnacademy.bookpubshop.tier.dummy;

import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;

/**
 * 회원등급 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class TierDummy {
    public static BookPubTier dummy() {
        return new BookPubTier(null, "tier", 1, 1L, 1000L);
    }
}
