package com.nhnacademy.bookpubshop.member.dummy;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;

/**
 * 멤버권한 관계테이블 더미 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class MemberAuthorityDummy {
    public static MemberAuthority dummy(Member member, Authority authority){
        return new MemberAuthority(new MemberAuthority.Pk(member.getMemberNo(),authority.getAuthorityNo()),
                member,authority);

    }
}
