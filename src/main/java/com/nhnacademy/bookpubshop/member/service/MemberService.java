package com.nhnacademy.bookpubshop.member.service;

import com.nhnacademy.bookpubshop.member.dto.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.DuplicateMemberFieldException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.NotFoundTierException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 레포지토리의 메소드를 이용하여 구현한 서버스입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final TierRepository tierRepository;

    private static final String TIER_NAME = "basic";

    /**
     * 데이터베이스에 멤버의 정보를 저장합니다.
     *
     * @param signUpMemberRequestDto member 엔티티에 들어갈 정보를 가지고 있는 DTO
     * @return member 테이블에 데이터를 저장한 후 반환받은 Member.
     */
    @Transactional
    public SignUpMemberResponseDto signup(SignUpMemberRequestDto signUpMemberRequestDto) {
        String nickname = signUpMemberRequestDto.getNickname();
        String email = signUpMemberRequestDto.getEmail();
        String id = signUpMemberRequestDto.getMemberId();

        BookPubTier tier = tierRepository.findByTierName(TIER_NAME)
                .orElseThrow(NotFoundTierException::new);

        duplicateCheck(nickname, email, id);

        Member member = signUpMemberRequestDto.createMember(tier);
        memberRepository.save(member);

        return new SignUpMemberResponseDto(
                member.getMemberId(),
                member.getMemberNickname(),
                member.getMemberEmail(),
                member.getTier().getTierName()
        );
    }

    private void duplicateCheck(String nickname, String email, String id) {
        if (memberRepository.existsByMemberNickname(nickname)) {
            throw new DuplicateMemberFieldException("닉네임(" + nickname + ")");
        }

        if (memberRepository.existsByMemberId(id)) {
            throw new DuplicateMemberFieldException("아이디(" + id + ")");
        }

        if (memberRepository.existsByMemberEmail(email)) {
            throw new DuplicateMemberFieldException("이메일(" + email + ")");
        }
    }
}
