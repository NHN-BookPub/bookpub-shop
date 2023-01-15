package com.nhnacademy.bookpubshop.member.dto.response;

import com.nhnacademy.bookpubshop.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버가 반환될때 보내야할 정보들이 담겨져있습니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class MemberDetailResponseDto {
    private Long memberNo;
    private String tierName;
    private String nickname;
    private String gender;
    private Integer birthMonth;
    private Integer birthYear;
    private String phone;
    private String email;
    private Long point;

    private List<String> authorities = new ArrayList<>();

    public MemberDetailResponseDto(Member member) {
        this.memberNo = member.getMemberNo();
        this.tierName = member.getTier().getTierName();
        this.nickname = member.getMemberNickname();
        this.gender = member.getMemberGender();
        this.birthMonth = member.getMemberBirthMonth();
        this.birthYear = member.getMemberBirthYear();
        this.phone = member.getMemberPhone();
        this.email = member.getMemberEmail();
        this.point = member.getMemberPoint();
        this.authorities = member.getMemberAuthorities().stream()
                .map(m-> m.getAuthority().getAuthorityName())
                .collect(Collectors.toList());
    }
}
