package com.nhnacademy.bookpubshop.member.dto;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
import lombok.Getter;

/**
 * member 정보 response DTO개체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
public class SignUpMemberResponseDto {
    private String name;
    private String nickname;
    private String birth;
    private String gender;
    private String memberId;
    private String pwd;
    private String phone;
    private String email;
    private String address;
    private String detailAddress;

    /**
     * 멤버 엔티티 생성 메소드.
     *
     * @param tier 멤버 생성 시 필요한 tier 객체.
     * @return Member entity 생성 후 반환
     */
    public Member createMember(Tier tier) {
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
