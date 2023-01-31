package com.nhnacademy.bookpubshop.member.dto.request;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;

/**
 * 회원가입 공통 클래스 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public abstract class SignupDto {
    public abstract Member createMember(BookPubTier tier);

    public abstract String getAddress();

    public abstract String getDetailAddress();

    public abstract String getNickname();

    public abstract String getMemberId();

    public Member getMember(BookPubTier tier, String birth, String memberId, String email, String gender, String name, String nickname, String phone, String pwd) {
        Integer memberYear = Integer.parseInt(birth.substring(0, 2));
        Integer memberMonthDay = Integer.parseInt(birth.substring(2));

        return Member.builder()
                .tier(tier)
                .memberId(memberId)
                .memberEmail(email)
                .memberGender(gender)
                .memberName(name)
                .memberNickname(nickname)
                .memberPhone(phone)
                .memberPwd(pwd)
                .memberBirthMonth(memberMonthDay)
                .memberBirthYear(memberYear)
                .build();
    }
}
