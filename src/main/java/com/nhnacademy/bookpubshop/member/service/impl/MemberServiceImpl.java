package com.nhnacademy.bookpubshop.member.service.impl;

import com.nhnacademy.bookpubshop.author.exception.AuthorityNotFoundException;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.authority.repository.AuthorityRepository;
import com.nhnacademy.bookpubshop.member.dto.request.LoginMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.EmailAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.IdAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.NicknameAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.TierNotFoundException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 멤버를 서비스레이어에서 사용하기위한 구현클래스입니다.
 *
 * @author : 임태원, 유호철
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TierRepository tierRepository;
    private final AuthorityRepository authorityRepository;

    private static final String TIER_NAME = "basic";
    private static final String AUTHORITY_NAME = "ROLE_MEMBER";

    /**
     * {@inheritDoc}
     *
     * @throws TierNotFoundException 등급이 없을때 나오는 에러.
     */
    @Transactional
    @Override
    public SignUpMemberResponseDto signup(SignUpMemberRequestDto signUpMemberRequestDto) {
        BookPubTier tier = tierRepository.findByTierName(TIER_NAME)
                .orElseThrow(TierNotFoundException::new);

        Authority authority = authorityRepository.findByAuthorityName(AUTHORITY_NAME)
                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NAME));

        duplicateCheck(signUpMemberRequestDto);

        Member member = signUpMemberRequestDto.createMember(tier);

        member.addMemberAuthority(new MemberAuthority(
                new MemberAuthority.Pk(member.getMemberNo(), authority.getAuthorityNo()),
                member,
                authority)
        );

        memberRepository.save(member);

        return new SignUpMemberResponseDto(
                member.getMemberId(),
                member.getMemberNickname(),
                member.getMemberEmail(),
                member.getTier().getTierName()
        );
    }

    /**
     * {@inheritDoc}
     *
     * @throws NicknameAlreadyExistsException 닉네임이 이미 존재할 때 나오는 에러.
     */
    @Transactional
    @Override
    public void modifyMemberNickName(Long memberNo, ModifyMemberNicknameRequestDto requestDto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        if (!Objects.equals(member.getMemberNickname(), requestDto.getNickname())
                && memberRepository.existsByMemberNickname(requestDto.getNickname())) {
            throw new NicknameAlreadyExistsException(requestDto.getNickname());
        }

        member.modifyNickname(requestDto.getNickname());
    }

    /**
     * {@inheritDoc}
     *
     * @throws EmailAlreadyExistsException 이메일이 이미 존재할 때 나오는 에러.
     */
    @Transactional
    @Override
    public void modifyMemberEmail(Long memberNo, ModifyMemberEmailRequestDto requestDto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        if (!Objects.equals(member.getMemberEmail(), requestDto.getEmail())
                && memberRepository.existsByMemberEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException(requestDto.getEmail());
        }

        member.modifyEmail(requestDto.getEmail());
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버를 찾지못함.
     */
    @Override
    public MemberDetailResponseDto getMemberDetails(Long memberNo) {
        return memberRepository.findByMemberDetails(memberNo)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberResponseDto> getMembers(Pageable pageable) {
        return memberRepository.findMembers(pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 없을때 나오는 에러.
     */
    @Override
    public void blockMember(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.memberBlock();
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 없을때 나오는 에러.
     */
    @Override
    public void deleteMember(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.memberDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginMemberResponseDto loginMember(LoginMemberRequestDto requestDto) {
        return memberRepository.findByMemberLoginInfo(requestDto.getMemberId(), requestDto.getPassword());
    }

    private void duplicateCheck(SignUpMemberRequestDto member) {
        if (memberRepository.existsByMemberNickname(member.getNickname())) {
            throw new NicknameAlreadyExistsException(member.getNickname());
        }

        if (memberRepository.existsByMemberId(member.getMemberId())) {
            throw new IdAlreadyExistsException(member.getMemberId());
        }

        if (memberRepository.existsByMemberEmail(member.getEmail())) {
            throw new EmailAlreadyExistsException(member.getEmail());
        }
    }

}
