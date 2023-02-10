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
    private String memberName;
    private String tierName;
    private String nickname;
    private String gender;
    private Integer birthMonth;
    private Integer birthYear;
    private String phone;
    private String email;
    private Long point;
    private List<String> authorities = new ArrayList<>();
    private List<MemberAddressResponseDto> addresses = new ArrayList<>();

    /**
     * 멤버정보를 반환하는 dto의 생성자 입니다. (entitiy to dto)
     *
     * @param member member 엔티티.
     */
    public MemberDetailResponseDto(Member member) {
        this.memberNo = member.getMemberNo();
        this.memberName = member.getMemberName();
        this.tierName = member.getTier().getTierName();
        this.nickname = member.getMemberNickname();
        this.gender = member.getMemberGender();
        this.birthMonth = member.getMemberBirthMonth();
        this.birthYear = member.getMemberBirthYear();
        this.phone = member.getMemberPhone();
        this.email = member.getMemberEmail();
        this.point = member.getMemberPoint();
        this.authorities = member.getMemberAuthorities().stream()
                .map(m -> m.getAuthority().getAuthorityName())
                .collect(Collectors.toList());
        this.addresses = member.getMemberAddress().stream()
                .map(MemberAddressResponseDto::new)
                .collect(Collectors.toList());
    }
}
