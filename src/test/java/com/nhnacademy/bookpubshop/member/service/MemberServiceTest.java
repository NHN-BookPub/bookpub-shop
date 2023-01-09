package com.nhnacademy.bookpubshop.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.member.dto.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.DuplicateMemberFieldException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.awt.print.Book;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 멤버 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(MemberService.class)
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    TierRepository tierRepository;
    SignUpMemberRequestDto signUpMemberRequestDto;
    final String duplicate = "중복되는 항목";
    Member member;
    BookPubTier tier;
    ArgumentCaptor<Member> captor;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        captor = ArgumentCaptor.forClass(Member.class);
        signUpMemberRequestDto = new SignUpMemberRequestDto();

        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");
    }

    @Test
    @DisplayName("멤버 생성 성공 테스트")
    void memberCreateSuccess() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);

        memberService.signup(signUpMemberRequestDto, tier.getTierName());

        verify(memberRepository, times(1))
                .save(captor.capture());

        Member result = captor.getValue();
        assertThat(signUpMemberRequestDto.getMemberId())
                .isEqualTo(result.getMemberId());
    }

    @Test
    @DisplayName("멤버 생성 아이디 중복으로 인한 실패 테스트")
    void memberCreateFailedDuplicateId() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(true);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto, tier.getTierName()))
                .isInstanceOf(DuplicateMemberFieldException.class)
                .hasMessageContaining(duplicate);
    }

    @Test
    @DisplayName("멤버 생성 닉네임 중복으로 인한 실패 테스트")
    void memberCreateFailedDuplicateNickname() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto, tier.getTierName()))
                .isInstanceOf(DuplicateMemberFieldException.class)
                .hasMessageContaining(duplicate);
    }

    @Test
    @DisplayName("멤버 생성 아이디 중복으로 인한 실패 테스트")
    void memberCreateFailedDuplicateEmail() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(true);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto, tier.getTierName()))
                .isInstanceOf(DuplicateMemberFieldException.class)
                .hasMessageContaining(duplicate);
    }
}