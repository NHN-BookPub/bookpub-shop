package com.nhnacademy.bookpubshop.member.relationship.dummy;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;

/**
 * 멤버 권한 연관관계 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class MemberAuthorityDummy {
    public static MemberAuthority dummy(Member member, Authority authority) {
        return new MemberAuthority(
                new MemberAuthority.Pk(1L, 1),
                member,
                authority
        );
    }
}
