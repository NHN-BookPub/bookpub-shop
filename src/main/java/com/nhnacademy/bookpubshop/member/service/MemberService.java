package com.nhnacademy.bookpubshop.member.service;

import com.nhnacademy.bookpubshop.member.dto.MemberSignupResponse;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
import com.nhnacademy.bookpubshop.tier.exception.NotFoundTierException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 레포지토리의 메소드를 이용하여 구현한 서버스입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TierRepository tierRepository;

    /**
     * 데이터베이스에 멤버의 정보를 저장합니다.
     *
     * @param memberSignupResponse member 엔티티에 들어갈 정보를 가지고 있는 DTO
     * @return member 테이블에 데이터를 저장한 후 반환받은 Member.
     */
    public Member signup(MemberSignupResponse memberSignupResponse) {
        Tier tier = tierRepository.findById(5)
                .orElseThrow(NotFoundTierException::new);

        Integer memberYear = Integer.parseInt(memberSignupResponse.getBirth().substring(0, 2));
        Integer memberMonthDay = Integer.parseInt(memberSignupResponse.getBirth().substring(2));

        Member member = Member.builder()
                .tier(tier)
                .memberId(memberSignupResponse.getMemberId())
                .memberEmail(memberSignupResponse.getEmail())
                .memberGender(memberSignupResponse.getGender())
                .memberName(memberSignupResponse.getName())
                .memberNickname(memberSignupResponse.getNickname())
                .memberPhone(memberSignupResponse.getPhone())
                .memberPwd(memberSignupResponse.getPwd())
                .memberBirthMonth(memberMonthDay)
                .memberBirthYear(memberYear)
                .build();

        return memberRepository.save(member);
    }
}
