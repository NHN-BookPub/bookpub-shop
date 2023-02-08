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

    /**
     * 회원가입 dto에서 member entity를 생성하는 메소드입니다.
     *
     * @param tier 등급
     * @param birth 생일
     * @param memberId 멤버 아이디
     * @param email 이메일
     * @param gender 성별
     * @param name 이름
     * @param nickname 닉네임
     * @param phone 전화번호
     * @param pwd 비밀번호
     * @return 회원 entity
     */
    public Member getMember(BookPubTier tier, String birth,
                            String memberId, String email,
                            String gender, String name,
                            String nickname, String phone, String pwd) {
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
