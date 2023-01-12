package com.nhnacademy.bookpubshop.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.EmailAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.IdAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.NicknameAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 멤버 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(MemberServiceImpl.class)
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
    ModifyMemberNicknameRequestDto nicknameRequestDto;

    ModifyMemberEmailRequestDto emailRequestDto;
    BookPubTier tier;
    ArgumentCaptor<Member> captor;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        captor = ArgumentCaptor.forClass(Member.class);
        signUpMemberRequestDto = new SignUpMemberRequestDto();
        nicknameRequestDto = new ModifyMemberNicknameRequestDto();
        emailRequestDto = new ModifyMemberEmailRequestDto();

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

        memberService.signup(signUpMemberRequestDto);

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

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto))
                .isInstanceOf(IdAlreadyExistsException.class)
                .hasMessageContaining(IdAlreadyExistsException.MESSAGE);
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

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto))
                .isInstanceOf(NicknameAlreadyExistsException.class)
                .hasMessageContaining(NicknameAlreadyExistsException.MESSAGE);
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

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(EmailAlreadyExistsException.MESSAGE);
    }

    @Test
    @DisplayName("멤버 아이디 수정 존재하지않는 아이디")
    void memberNickNameCheckFailNotFoundTest(){
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.modifyMemberNickName(1L, nicknameRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("멤버 아이디 수정 이미 존재하는 닉네임")
    void memberNickNameCheckFailExistsNickName() {
        ReflectionTestUtils.setField(nicknameRequestDto,"nickname","nick");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> memberService.modifyMemberNickName(1L, nicknameRequestDto))
                .isInstanceOf(NicknameAlreadyExistsException.class)
                .hasMessageContaining(NicknameAlreadyExistsException.MESSAGE);

        verify(memberRepository, times(1))
                .findById(1L);
        verify(memberRepository, times(1))
                .existsByMemberNickname(nicknameRequestDto.getNickname());
    }

    @DisplayName("멤버 닉네임 수정 성공")
    @Test
    void memberNicknameSuccess() {
        ReflectionTestUtils.setField(nicknameRequestDto,"nickname",member.getMemberNickname());
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);

        memberService.modifyMemberNickName(1L,nicknameRequestDto);

        verify(memberRepository, times(1))
                .findById(1L);
    }

    @DisplayName("멤버 이메일 수정 멤버 찾기 실패 테스트")
    @Test
    void memberEmailModifyFailNotFoundTest() {

        ReflectionTestUtils.setField(emailRequestDto, "email", member.getMemberEmail());

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.modifyMemberEmail(1L, emailRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("멤버 이메일 수정 관련 이메일 이미 존재")
    @Test
    void memberEmailAlreadyExistsTest() {
        ReflectionTestUtils.setField(emailRequestDto, "email", "tets");

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> memberService.modifyMemberEmail(1L, emailRequestDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(EmailAlreadyExistsException.MESSAGE);
    }

    @DisplayName("멤버 이메일 수정 관련 성공")
    @Test
    void memberEmailSuccessTest() {
        ReflectionTestUtils.setField(emailRequestDto,"email",member.getMemberEmail());

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);

        memberService.modifyMemberEmail(1L, emailRequestDto);

        verify(memberRepository, times(1))
                .findById(1L);
    }

    @DisplayName("멤버 상세 정보조회실패")
    @Test
    void memberDetailFailTest() {
        when(memberRepository.findByMemberDetails(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getMemberDetails(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("멤버 상세정보 조회 성공")
    @Test
    void memberDetailsSuccess() {

        MemberDetailResponseDto dto = new MemberDetailResponseDto(1L,
                "tier",
                "nick",
                "gender",
                1,
                1,
                "010",
                "email",
                1L,
                "authority");
        when(memberRepository.findByMemberDetails(anyLong()))
                .thenReturn(Optional.of(dto));

        MemberDetailResponseDto result = memberService.getMemberDetails(1L);

        assertThat(result.getMemberNo()).isEqualTo(dto.getMemberNo());
        assertThat(result.getPhone()).isEqualTo(dto.getPhone());
        assertThat(result.getPoint()).isEqualTo(dto.getPoint());
        assertThat(result.getGender()).isEqualTo(dto.getGender());
        assertThat(result.getTierName()).isEqualTo(dto.getTierName());
        assertThat(result.getAuthority()).isEqualTo(dto.getAuthority());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getBirthYear()).isEqualTo(dto.getBirthYear());
        assertThat(result.getBirthMonth()).isEqualTo(dto.getBirthMonth());
        assertThat(result.getNickname()).isEqualTo(dto.getNickname());

        verify(memberRepository, times(1))
                .findByMemberDetails(1L);
    }

    @DisplayName("전체 멤버 조회 테스트")
    @Test
    void getMemberTest() {
        Pageable pageable = PageRequest.of(0, 10);

        MemberResponseDto memberResponseDto =
                new MemberResponseDto(1L, "tier",
                        "id", "nick", "name", "gender",
                        1, 1, "email", 1L, true,true,true);

        PageImpl<MemberResponseDto> page = new PageImpl<>(List.of(memberResponseDto), pageable,1);
        when(memberRepository.findMembers(pageable))
                .thenReturn(page);

        Page<MemberResponseDto> result = memberService.getMembers(pageable);

        List<MemberResponseDto> content = result.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getMemberNo()).isEqualTo(memberResponseDto.getMemberNo());
        assertThat(content.get(0).getTier()).isEqualTo(memberResponseDto.getTier());
        assertThat(content.get(0).getMemberId()).isEqualTo(memberResponseDto.getMemberId());
        assertThat(content.get(0).getNickname()).isEqualTo(memberResponseDto.getNickname());
        assertThat(content.get(0).getName()).isEqualTo(memberResponseDto.getName());
        assertThat(content.get(0).getGender()).isEqualTo(memberResponseDto.getGender());
        assertThat(content.get(0).getBirthYear()).isEqualTo(memberResponseDto.getBirthYear());
        assertThat(content.get(0).getBirthMonth()).isEqualTo(memberResponseDto.getBirthMonth());
        assertThat(content.get(0).getEmail()).isEqualTo(memberResponseDto.getEmail());
        assertThat(content.get(0).getPoint()).isEqualTo(memberResponseDto.getPoint());
        assertThat(content.get(0).isSocial()).isEqualTo(memberResponseDto.isSocial());

        verify(memberRepository, times(1))
                .findMembers(pageable);
    }

    @DisplayName("멤버 차단 실패 테스트")
    @Test
    void blockMemberFailTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.blockMember(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("멤버 차단 성공")
    @Test
    void blockMemberSuccessTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        memberService.blockMember(1L);

        verify(memberRepository,times(1))
                .findById(1L);
    }

    @DisplayName("멤버 탈퇴 실패")
    @Test
    void deleteMemberFailTest(){
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.blockMember(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("멤버 탈퇴 성공")
    @Test
    void deleteMemberSuccessTest(){
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        memberService.deleteMember(1L);

        then(memberRepository).should().findById(1L);
    }
}