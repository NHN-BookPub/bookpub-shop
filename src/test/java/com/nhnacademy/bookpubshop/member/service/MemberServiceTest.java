package com.nhnacademy.bookpubshop.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.address.exception.AddressNotFoundException;
import com.nhnacademy.bookpubshop.address.repository.AddressRepository;
import com.nhnacademy.bookpubshop.authority.dummy.AuthorityDummy;
import com.nhnacademy.bookpubshop.authority.repository.AuthorityRepository;
import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPasswordRequest;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPhoneRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.OauthMemberCreateRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberAuthResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberPasswordResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.AuthorityNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.IdAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.NicknameAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * ?????? ????????? ?????????.
 *
 * @author : ?????????
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
    @MockBean
    AddressRepository addressRepository;
    @MockBean
    RedisTemplate<String, String> redisTemplate;
    @MockBean
    ObjectMapper objectMapper;
    @MockBean
    AuthorityRepository authorityRepository;

    SignUpMemberRequestDto signUpMemberRequestDto;
    Member member;
    ModifyMemberNicknameRequestDto nicknameRequestDto;

    Address address;

    ModifyMemberEmailRequestDto emailRequestDto;
    MemberAuthResponseDto authResponseDto;
    MemberResponseDto memberResponseDto;
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
        address = AddressDummy.dummy(member);

        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        memberResponseDto = new MemberResponseDto(1L, "tier", "memberId", "nickname", "name", "gender", 1999, 1111, "email", 100L, false, false, false);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void memberCreateSuccess() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);
        when(authorityRepository.findByAuthorityName(anyString()))
                .thenReturn(Optional.of(AuthorityDummy.dummy()));

        when(memberRepository.save(any())).thenReturn(member);

        memberService.signup(signUpMemberRequestDto);

        verify(memberRepository, times(1))
                .save(captor.capture());

        Member result = captor.getValue();
        assertThat(signUpMemberRequestDto.getMemberId())
                .isEqualTo(result.getMemberId());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void memberCreateFailed() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);
        when(authorityRepository.findByAuthorityName(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto))
                .isInstanceOf(AuthorityNotFoundException.class)
                .hasMessageContaining(AuthorityNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("oauth ?????? ?????? ?????? ?????????")
    void oauthMemberCreateSuccess() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);
        when(authorityRepository.findByAuthorityName(anyString()))
                .thenReturn(Optional.of(AuthorityDummy.dummy()));

        when(memberRepository.save(any())).thenReturn(member);

        OauthMemberCreateRequestDto createRequestDto = new OauthMemberCreateRequestDto();

        ReflectionTestUtils.setField(createRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(createRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(createRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(createRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(createRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(createRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(createRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(createRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(createRequestDto, "address", "??????");
        ReflectionTestUtils.setField(createRequestDto, "detailAddress", "109??? 102???");

        memberService.signup(createRequestDto);

        verify(memberRepository, times(1))
                .save(captor.capture());

        Member result = captor.getValue();
        assertThat(createRequestDto.getMemberId())
                .isEqualTo(result.getMemberId());
    }


    @Test
    @DisplayName("?????? ?????? ????????? ???????????? ?????? ?????? ?????????")
    void memberCreateFailedDuplicateId() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(true);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);
        when(authorityRepository.findByAuthorityName(anyString()))
                .thenReturn(Optional.of(AuthorityDummy.dummy()));
        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto))
                .isInstanceOf(IdAlreadyExistsException.class)
                .hasMessageContaining(IdAlreadyExistsException.MESSAGE);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ???????????? ?????? ?????? ?????????")
    void memberCreateFailedDuplicateNickname() {
        when(tierRepository.findByTierName(anyString()))
                .thenReturn(Optional.of(tier));
        when(memberRepository.existsByMemberEmail(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(false);
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(true);
        when(authorityRepository.findByAuthorityName(anyString()))
                .thenReturn(Optional.of(AuthorityDummy.dummy()));

        assertThatThrownBy(() -> memberService.signup(signUpMemberRequestDto))
                .isInstanceOf(NicknameAlreadyExistsException.class)
                .hasMessageContaining(NicknameAlreadyExistsException.MESSAGE);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????????????? ?????????")
    void memberNickNameCheckFailNotFoundTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.modifyMemberNickName(1L, nicknameRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ???????????? ?????????")
    void memberNickNameCheckFailExistsNickName() {
        ReflectionTestUtils.setField(nicknameRequestDto, "nickname", "nick");
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

    @DisplayName("?????? ????????? ?????? ??????")
    @Test
    void memberNicknameSuccess() {
        ReflectionTestUtils.setField(nicknameRequestDto, "nickname", member.getMemberNickname());
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(false);

        memberService.modifyMemberNickName(1L, nicknameRequestDto);

        verify(memberRepository, times(1))
                .findById(1L);
    }

    @DisplayName("?????? ????????? ?????? ?????? ?????? ?????? ?????????")
    @Test
    void memberEmailModifyFailNotFoundTest() {

        ReflectionTestUtils.setField(emailRequestDto, "email", member.getMemberEmail());

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.modifyMemberEmail(1L, emailRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ????????? ?????? ?????? ??????")
    @Test
    void memberEmailSuccessTest() {
        ReflectionTestUtils.setField(emailRequestDto, "email", "modify_email");

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        memberService.modifyMemberEmail(1L, emailRequestDto);

        verify(memberRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("?????? ?????? ??????????????????")
    @Test
    void memberDetailFailTest() {
        when(memberRepository.findByMemberDetails(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getMemberDetails(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void memberDetailsSuccess() {

        MemberDetailResponseDto dto = MemberDummy.memberDetailResponseDummy();
        when(memberRepository.findByMemberDetails(anyLong()))
                .thenReturn(Optional.of(dto));

        MemberDetailResponseDto result = memberService.getMemberDetails(1L);

        assertThat(result.getMemberNo()).isEqualTo(dto.getMemberNo());
        assertThat(result.getPhone()).isEqualTo(dto.getPhone());
        assertThat(result.getPoint()).isEqualTo(dto.getPoint());
        assertThat(result.getGender()).isEqualTo(dto.getGender());
        assertThat(result.getTierName()).isEqualTo(dto.getTierName());
        assertThat(result.getAuthorities()).isEqualTo(dto.getAuthorities());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getBirthYear()).isEqualTo(dto.getBirthYear());
        assertThat(result.getBirthMonth()).isEqualTo(dto.getBirthMonth());
        assertThat(result.getNickname()).isEqualTo(dto.getNickname());

        verify(memberRepository, times(1))
                .findByMemberDetails(1L);
    }

    @DisplayName("?????? ?????? ?????? ?????????")
    @Test
    void getMemberTest() {
        Pageable pageable = PageRequest.of(0, 10);

        MemberResponseDto memberResponseDto =
                new MemberResponseDto(1L, "tier",
                        "id", "nick", "name", "gender",
                        1, 1, "email", 1L, true, true, true);

        PageImpl<MemberResponseDto> page = new PageImpl<>(List.of(memberResponseDto), pageable, 1);
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

    @DisplayName("?????? ?????? ?????? ?????????")
    @Test
    void blockMemberFailTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.blockMember(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void blockMemberSuccessTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        memberService.blockMember(1L);

        verify(memberRepository, times(1))
                .findById(1L);
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void deleteMemberFailTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.blockMember(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void deleteMemberSuccessTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        memberService.deleteMember(1L);

        then(memberRepository).should().findById(1L);
    }

    @DisplayName("????????? ?????? ????????????")
    @Test
    void loginMemberTest() {
        LoginMemberResponseDto loginMemberInfo = MemberDummy.dummy2();

        when(memberRepository.findByMemberLoginInfo(anyString()))
                .thenReturn(loginMemberInfo);

        LoginMemberResponseDto result = memberService.loginMember("test");

        assertThat(result.getMemberId()).isEqualTo(loginMemberInfo.getMemberId());
        assertThat(result.getMemberPwd()).isEqualTo(loginMemberInfo.getMemberPwd());
        assertThat(result.getAuthorities()).isEqualTo(loginMemberInfo.getAuthorities());
        assertThat(result.getMemberNo()).isEqualTo(loginMemberInfo.getMemberNo());

        verify(memberRepository, times(1))
                .findByMemberLoginInfo("test");
    }


    @DisplayName("????????? ?????? ??????")
    @Test
    void getMemberStatisticsTest() {
        //given
        MemberStatisticsResponseDto dto = MemberDummy.memberStatisticsDummy();
        when(memberRepository.memberStatistics())
                .thenReturn(dto);

        MemberStatisticsResponseDto result = memberService.getMemberStatistics();

        assertThat(result.getBlockMemberCnt()).isEqualTo(dto.getBlockMemberCnt());
        assertThat(result.getMemberCnt()).isEqualTo(dto.getMemberCnt());
        assertThat(result.getCurrentMemberCnt()).isEqualTo(dto.getCurrentMemberCnt());
        assertThat(result.getDeleteMemberCnt()).isEqualTo(dto.getDeleteMemberCnt());
    }

    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void getMemberTierStatisticsTest() {
        MemberTierStatisticsResponseDto dto = MemberDummy.memberTierStatisticsDummy();
        when(memberRepository.memberTierStatistics())
                .thenReturn(List.of(dto));

        List<MemberTierStatisticsResponseDto> result = memberService.getTierStatistics();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTierCnt()).isEqualTo(dto.getTierCnt());
        assertThat(result.get(0).getTierValue()).isEqualTo(dto.getTierValue());
        assertThat(result.get(0).getTierName()).isEqualTo(dto.getTierName());
    }

    @DisplayName("?????? ?????????????????? ?????? ?????? ????????? ??????")
    @Test
    void modifyMemberTestFail() {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "10101010");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.modifyMemberPhone(1L, dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);

        then(memberRepository).should().findById(1L);
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void modifyMemberTestSuccess() {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "12345");

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        memberService.modifyMemberName(1L, dto);

        then(memberRepository).should().findById(1L);
    }


    @DisplayName("????????? ?????? ??????")
    @Test
    void idDuplicateCheckTest() {
        when(memberRepository.existsByMemberId(anyString()))
                .thenReturn(true);

        boolean isDuplicateId = memberService.idDuplicateCheck("tagkdj1");

        assertThat(isDuplicateId).isTrue();
    }

    @DisplayName("????????? ?????? ??????")
    @Test
    void nicknameDuplicateCheckTest() {
        when(memberRepository.existsByMemberNickname(anyString()))
                .thenReturn(true);

        boolean isDuplicateNick = memberService.nickNameDuplicateCheck("taewon");

        assertThat(isDuplicateNick).isTrue();
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void getMemberPwdTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.getMemberPwd(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void getMemberPwdSuccessTest() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        MemberPasswordResponseDto memberPwd = memberService.getMemberPwd(1L);

        assertThat(member.getMemberPwd()).isEqualTo(memberPwd.getPassword());
    }

    @DisplayName("?????? ?????? ?????? ??????")
    @Test
    void getMemberNameFailTest() {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "name");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.modifyMemberName(1L, dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ?????? ?????? ??????")
    @Test
    void getMemberNameSuccessTest() {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "name");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        memberService.modifyMemberName(1L, dto);

        then(memberRepository).should().findById(1L);

    }

    @DisplayName("?????? ??????????????? ?????? ??????")
    @Test
    void getMemberPhoneFailTest() {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "01010101011");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.modifyMemberPhone(1L, dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ??????????????? ?????? ??????")
    @Test
    void getMemberPhoneSuccessTest() {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "01010101011");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        memberService.modifyMemberPhone(1L, dto);

        then(memberRepository).should().findById(1L);
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void getMemberPasswordFailTest() {
        ModifyMemberPasswordRequest dto = new ModifyMemberPasswordRequest();
        ReflectionTestUtils.setField(dto, "password", "password");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.modifyMemberPassword(1L, dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void getMemberPasswordSuccessTest() {
        ModifyMemberPasswordRequest dto = new ModifyMemberPasswordRequest();
        ReflectionTestUtils.setField(dto, "password", "password");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        memberService.modifyMemberPassword(1L, dto);

        then(memberRepository).should().findById(1L);
    }

    @DisplayName("??????????????? ?????? ????????? ??????")
    @Test
    void findMemberBaseAddressFailTest() {
        when(addressRepository.findByMemberBaseAddress(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.modifyMemberBaseAddress(1L, 1L))
                .isInstanceOf(AddressNotFoundException.class)
                .hasMessageContaining(AddressNotFoundException.MESSAGE);
    }

    @DisplayName("????????? ?????? ?????? ?????? ?????????")
    @Test
    void findMemberBaseAddressFail2Test() {
        when(addressRepository.findByMemberBaseAddress(anyLong()))
                .thenReturn(Optional.of(address));
        when(addressRepository.findByMemberExchangeAddress(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.modifyMemberBaseAddress(1L, 1L))
                .isInstanceOf(AddressNotFoundException.class)
                .hasMessageContaining(AddressNotFoundException.MESSAGE);
    }

    @DisplayName("????????? ?????? ????????? ??????")
    @Test
    void modifyMemberBaseAddressSuccessTest() {
        when(addressRepository.findByMemberBaseAddress(anyLong()))
                .thenReturn(Optional.of(address));
        when(addressRepository.findByMemberExchangeAddress(anyLong(), anyLong()))
                .thenReturn(Optional.of(address));

        memberService.modifyMemberBaseAddress(1L, 1L);

        then(addressRepository).should().findByMemberBaseAddress(1L);
        then(addressRepository).should().findByMemberExchangeAddress(1L, 1L);
    }

    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void memberAddressAddFailTest() {
        CreateAddressRequestDto dto = AddressDummy.createAddressDtoDummy();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.addMemberAddress(1L, dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("????????? ????????? ?????? ??????")
    @Test
    void memberAddressAddSuccessTest() {
        CreateAddressRequestDto dto = AddressDummy.createAddressDtoDummy();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        memberService.addMemberAddress(1L, dto);

        then(memberRepository).should().findById(1L);
    }

    @DisplayName("????????? ?????? ?????? ????????? Fail ")
    @Test
    void memberAddressDeleteFailTest() {
        when(addressRepository.findByMemberExchangeAddress(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.deleteMemberAddress(1L, 1L))
                .isInstanceOf(AddressNotFoundException.class)
                .hasMessageContaining(AddressNotFoundException.MESSAGE);
    }

    @DisplayName("????????? ?????? ?????? ????????? Success")
    @Test
    void memberAddressDeleteSuccessTest() {
        when(addressRepository.findByMemberExchangeAddress(anyLong(), anyLong()))
                .thenReturn(Optional.of(address));
        doNothing().when(addressRepository)
                .delete(any(Address.class));
        memberService.deleteMemberAddress(1L, 1L);

        then(addressRepository).should().findByMemberExchangeAddress(1L, 1L);
        then(addressRepository).should().delete(address);
    }

    @Test
    @DisplayName("?????? ????????? ????????? auth ?????? ?????? ?????????")
    void authMemberInfoTest() {
        authResponseDto = new MemberAuthResponseDto(1L, "password", List.of("admin"));

        when(memberRepository.findByAuthMemberInfo(anyLong()))
                .thenReturn(authResponseDto);

        memberService.authMemberInfo(1L);

        verify(memberRepository, times(1)).findByAuthMemberInfo(anyLong());
    }

    @DisplayName("oauth ?????? ?????? ?????????")
    @Test
    void isOauthMember() {
        when(memberRepository.existsByMemberId(anyString())).thenReturn(true);

        boolean oauthMember = memberService.isOauthMember("tagkdj1@naver.com");

        assertThat(oauthMember).isTrue();

        verify(memberRepository, times(1))
                .existsByMemberId("tagkdj1@naver.com");
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????? ?????? ?????????")
    void getTierByMemberNoTest() {
        when(memberRepository.findTierNoByMemberNo(anyLong())).thenReturn(1);

        Integer result = memberService.getTierByMemberNo(1L);

        assertThat(result).isEqualTo(1);

        verify(memberRepository, times(1)).findTierNoByMemberNo(anyLong());
    }

    @Test
    @DisplayName("?????? ??????????????? ?????? ?????? ????????? ?????? ?????? ?????????")
    void getMembersByNickNameTest() {
        Pageable pagable = PageRequest.of(0, 10);
        PageImpl<MemberResponseDto> page = new PageImpl<>(List.of(memberResponseDto), pagable, 1);

        when(memberRepository.findMembersListByNickName(any(), anyString())).thenReturn(page);

        memberService.getMembersByNickName(pagable, "search");

        verify(memberRepository, times(1)).findMembersListByNickName(any(), anyString());
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ??????????????? ?????? ?????? ?????????")
    void getMembersById() {
        Pageable pagable = PageRequest.of(0, 10);
        PageImpl<MemberResponseDto> page = new PageImpl<>(List.of(memberResponseDto), pagable, 1);

        when(memberRepository.findMembersListById(any(), anyString())).thenReturn(page);

        memberService.getMembersById(pagable, "search");

        verify(memberRepository, times(1)).findMembersListById(any(), anyString());
    }
}