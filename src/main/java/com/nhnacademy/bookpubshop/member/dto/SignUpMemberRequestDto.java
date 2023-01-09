package com.nhnacademy.bookpubshop.member.dto;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

/**
 * member 정보 response DTO개체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
public class SignUpMemberRequestDto {
    @NotBlank
    @Pattern(regexp = "^.*(?=.*[가-힣a-z])(?=.{2,200}).*$",
            message = "이름은 한글 또는 영어 2글자 이상 200글자 이하로 입력해주세요.")
    private String name;

    @NotBlank
    @Pattern(regexp = "^.*(?=.*[a-z])(?=.*[a-z\\d])(?=.{2,8}).*$",
            message = "닉네임은 영어는 필수 숫자는 선택으로 2글자 이상 8글자 이하로 입력해주세요.")
    private String nickname;

    @NotBlank
    @Length(min = 6, max = 6, message = "생년월일은 숫자로 6글자 입력해주세요")
    private String birth;

    @NotBlank
    @Length(min = 2, max = 2, message = "성별의 길이는 2글자로 입력해주세요")
    private String gender;

    @NotBlank
    @Pattern(regexp = "^.*(?=.*[a-z])(?=.*\\d)(?=.{5,20}).*$",
            message = "아이디는 영어와 숫자를 섞어 5글자에서 20글자로 입력해주세요.")
    private String memberId;

    @NotBlank
    private String pwd;

    @NotBlank
    @Length(min = 11, max = 11, message = "전화번호는 숫자 11글자로 입력해주세요.")
    private String phone;

    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String detailAddress;

    public SignUpMemberRequestDto() {
    }

    /**
     * 멤버 엔티티 생성 메소드.
     *
     * @param tier 멤버 생성 시 필요한 tier 객체.
     * @return Member entity 생성 후 반환
     */
    public Member createMember(BookPubTier tier) {
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
