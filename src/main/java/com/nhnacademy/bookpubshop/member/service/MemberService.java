package com.nhnacademy.bookpubshop.member.service;

import com.nhnacademy.bookpubshop.member.dto.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.DuplicateMemberFieldException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
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

    /**
     * 데이터베이스에 멤버의 정보를 저장합니다.
     *
     * @param signUpMemberResponseDto member 엔티티에 들어갈 정보를 가지고 있는 DTO
     * @return member 테이블에 데이터를 저장한 후 반환받은 Member.
     */
    @Transactional
    public Member signup(SignUpMemberResponseDto signUpMemberResponseDto) {
        String nickname = signUpMemberResponseDto.getNickname();
        String email = signUpMemberResponseDto.getEmail();
        String id = signUpMemberResponseDto.getMemberId();

        Tier tier = tierRepository.findById(5)
                .orElseThrow(NotFoundTierException::new);

        duplicateCheck(nickname, email, id);

        Member member = signUpMemberResponseDto.createMember(tier);

        return memberRepository.save(member);
    }

    private void duplicateCheck(String nickname, String email, String id) {
        memberRepository.findByMemberNickname(nickname)
                .orElseThrow(() -> new DuplicateMemberFieldException("닉네임(" + nickname + ")"));
        memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new DuplicateMemberFieldException("이메일(" + email + ")"));
        memberRepository.findByMemberId(id)
                .orElseThrow(() -> new DuplicateMemberFieldException("아이디(" + id + ")"));
    }
}
