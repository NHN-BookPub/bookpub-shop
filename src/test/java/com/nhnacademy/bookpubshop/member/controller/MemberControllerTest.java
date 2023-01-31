package com.nhnacademy.bookpubshop.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.IdRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.LoginMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPasswordRequest;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPhoneRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.NickRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.OauthMemberCreateRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberPasswordResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 멤버 컨트롤러 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@WebMvcTest(MemberController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    String path = "/api/signup";
    String oauthPath = "/api/oauth/signup";

    BookPubTier basic;

    SignUpMemberRequestDto signUpMemberRequestDto;
    OauthMemberCreateRequestDto oauthMemberCreateRequestDto;

    SignUpMemberResponseDto signUpMemberResponseDto;
    SignUpMemberResponseDto oauthSignUpMemberResponseDto;

    @BeforeEach
    void setUp() {
        basic = new BookPubTier("basic",1,1L,100L);
        objectMapper = new ObjectMapper();
        signUpMemberRequestDto = new SignUpMemberRequestDto();
        oauthMemberCreateRequestDto = new OauthMemberCreateRequestDto();

        signUpMemberResponseDto = new SignUpMemberResponseDto(
                "tagkdj1",
                "taewon",
                "tagkdj1@naver.com",
                "basic"
        );
        oauthSignUpMemberResponseDto = new SignUpMemberResponseDto(
                "tagkdj1@github.com",
                "taewon",
                "tagkdj1@naver.com",
                "basic"
        );
    }

    @Test
    @DisplayName("멤버 생성완료")
    void memberCreateSuccess() throws Exception {
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

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.memberId").value("tagkdj1"))
                .andExpect(jsonPath("$.memberNickname").value("taewon"))
                .andExpect(jsonPath("$.memberEmail").value("tagkdj1@naver.com"))
                .andExpect(jsonPath("$.tierName").value("basic"));
    }

    @Test
    @DisplayName("이름 validation 에러로 인한 실패")
    void memberCreateFailedBecauseNameValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("이름은 한글 또는 영어 2글자 이상 200글자 이하로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 validation 에러로 인한 실패")
    void memberCreateFailedBecauseNicknameValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "abc123Abc12");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("닉네임은 영어나 숫자로 2글자 이상 8글자 이하로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("성별 validation 에러로 인한 실패")
    void memberCreateFailedBecauseGenderValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성여성합쳐진혼종괴물");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("성별의 길이는 2글자로 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("생일 validation 에러로 인한 실패")
    void memberCreateFailedBecauseBirthValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "19981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("생년월일은 숫자로 6글자 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("아이디 validation 에러로 인한 실패")
    void memberCreateFailedBecauseIdValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagk");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("아이디는 영어나 숫자로 5글자에서 20글자로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("전화번호 validation 에러로 인한 실패")
    void memberCreateFailedBecausePhoneValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "4358-0106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("전화번호는 숫자 11글자로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 변경시 Validation error ")
    void memberModifyNickNameValidException() throws Exception {
        ModifyMemberNicknameRequestDto dto = new ModifyMemberNicknameRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", null);

        doNothing().when(memberService)
                .modifyMemberNickName(anyLong(), any());

        mvc.perform(put("/api/members/{memberNo}/nickName", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("닉네임은 null 이 될수없습니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("닉네임 문법에 안맞음")
    void memberModifyNickNameNotCompletedExceptionTest() throws Exception {

        ModifyMemberNicknameRequestDto dto = new ModifyMemberNicknameRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "가나다라마바사");

        doNothing().when(memberService)
                .modifyMemberNickName(anyLong(), any());

        mvc.perform(put("/api/members/{memberNo}/nickName", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("닉네임은 영어는 필수 숫자는 선택으로 2글자 이상 8글자 이하로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 수정성공 테스트")
    void memberModifyNickNameNotCompletedTest() throws Exception {

        ModifyMemberNicknameRequestDto dto = new ModifyMemberNicknameRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "asdf");

        doNothing().when(memberService)
                .modifyMemberNickName(1L, dto);

        mvc.perform(put("/api/members/{memberNo}/nickName", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(memberService).should().modifyMemberNickName(anyLong(), any());
    }

    @Test
    @DisplayName("이메일 수정 실패 테스트")
    void memberModifyEmailNotCompletedTest() throws Exception {
        ModifyMemberEmailRequestDto dto = new ModifyMemberEmailRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a");
        doNothing().when(memberService)
                .modifyMemberEmail(anyLong(), any());

        mvc.perform(put("/api/members/{memberNo}/email", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("이메일 형식으로 입력해야합니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("이메일 수정 성공 테스트")
    void memberEmailSuccessTest() throws Exception {

        ModifyMemberEmailRequestDto dto = new ModifyMemberEmailRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        doNothing().when(memberService)
                .modifyMemberEmail(1L, dto);

        mvc.perform(put("/api/members/{memberNo}/email", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(memberService).should().modifyMemberEmail(anyLong(), any());
    }

    @Test
    @DisplayName("멤버 상세 조회 성공 테스트")
    void memberDetailsTest() throws Exception {
        MemberDetailResponseDto dto = MemberDummy.memberDetailResponseDummy();
        when(memberService.getMemberDetails(1L))
                .thenReturn(dto);

        mvc.perform(get("/api/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberNo").value(objectMapper.writeValueAsString(dto.getMemberNo())))
                .andExpect(jsonPath("$.tierName").value(dto.getTierName()))
                .andExpect(jsonPath("$.nickname").value(dto.getNickname()))
                .andExpect(jsonPath("$.gender").value(dto.getGender()))
                .andExpect(jsonPath("$.birthMonth").value(objectMapper.writeValueAsString(dto.getBirthMonth())))
                .andExpect(jsonPath("$.birthYear").value(objectMapper.writeValueAsString(dto.getBirthYear())))
                .andExpect(jsonPath("$.phone").value(dto.getPhone()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.point").value(objectMapper.writeValueAsString(dto.getPoint())))
                .andExpect(jsonPath("$.authorities").value(dto.getAuthorities()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(memberService)
                .should().getMemberDetails(anyLong());
    }

    @Test
    @DisplayName("전체 멤버를 조회하는 메서드입니다.")
    void memberListTest() throws Exception {
        MemberResponseDto dto = new MemberResponseDto(1L, "tier", "id", "nick",
                "name", "gender", 1, 1, "email",
                1L, false, false, false);
        List<MemberResponseDto> content = List.of(dto);
        PageRequest request = PageRequest.of(0, 10);

        PageImpl<MemberResponseDto> page = new PageImpl<>(content, request, 1);

        when(memberService.getMembers(request))
                .thenReturn(page);

        mvc.perform(get("/api/admin/members")
                        .param("page", objectMapper.writeValueAsString(request.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(request.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[0].memberNo").value(objectMapper.writeValueAsString(content.get(0).getMemberNo())))
                .andExpect(jsonPath("$.content[0].tier").value(content.get(0).getTier()))
                .andExpect(jsonPath("$.content[0].memberId").value(content.get(0).getMemberId()))
                .andExpect(jsonPath("$.content[0].nickname").value(content.get(0).getNickname()))
                .andExpect(jsonPath("$.content[0].name").value(content.get(0).getName()))
                .andExpect(jsonPath("$.content[0].gender").value(content.get(0).getGender()))
                .andExpect(jsonPath("$.content[0].birthYear").value(objectMapper.writeValueAsString(content.get(0).getBirthYear())))
                .andExpect(jsonPath("$.content[0].birthMonth").value(objectMapper.writeValueAsString(content.get(0).getBirthMonth())))
                .andExpect(jsonPath("$.content[0].email").value(content.get(0).getEmail()))
                .andExpect(jsonPath("$.content[0].point").value(objectMapper.writeValueAsString(content.get(0).getPoint())))
                .andExpect(jsonPath("$.content[0].social").value(content.get(0).isSocial()))
                .andExpect(jsonPath("$.content[0].deleted").value(content.get(0).isDeleted()))
                .andExpect(jsonPath("$.content[0].blocked").value(content.get(0).isBlocked()))
                .andDo(print());

        then(memberService)
                .should()
                .getMembers(any());
    }

    @Test
    @DisplayName("멤버 차단 및 복구를 수행하는 메서드 성공")
    void memberBlockSuccessTest() throws Exception {
        doNothing().when(memberService)
                .blockMember(1L);

        mvc.perform(put("/api/admin/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("멤버별 통계 ")
    void memberStatisticsTest() throws Exception {
        MemberStatisticsResponseDto dto = MemberDummy.memberStatisticsDummy();
        when(memberService.getMemberStatistics())
                .thenReturn(dto);

        mvc.perform(get("/api/admin/members/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberCnt").value(dto.getMemberCnt()))
                .andExpect(jsonPath("$.currentMemberCnt").value(dto.getCurrentMemberCnt()))
                .andExpect(jsonPath("$.deleteMemberCnt").value(dto.getDeleteMemberCnt()))
                .andExpect(jsonPath("$.blockMemberCnt").value(dto.getBlockMemberCnt()))
                .andDo(print());

        then(memberService)
                .should().getMemberStatistics();
    }

    @Test
    @DisplayName("멤버 등급별 통계")
    void memberTierStatisticsTest() throws Exception {

        MemberTierStatisticsResponseDto dto = MemberDummy.memberTierStatisticsDummy();
        when(memberService.getTierStatistics())
                .thenReturn(List.of(dto));

        mvc.perform(get("/api/admin/tier/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tierName").value(dto.getTierName()))
                .andExpect(jsonPath("$[0].tierValue").value(objectMapper.writeValueAsString(dto.getTierValue())))
                .andExpect(jsonPath("$[0].tierCnt").value(objectMapper.writeValueAsString(dto.getTierCnt())))
                .andDo(print());

        then(memberService)
                .should().getTierStatistics();
    }

    @Test
    @DisplayName("로그인 요청을 한 멤버의 정보를 조회")
    void memberLoginSuccessTest() throws Exception {
        LoginMemberRequestDto login = new LoginMemberRequestDto();
        LoginMemberResponseDto loginDummy = MemberDummy.dummy2();

        ReflectionTestUtils.setField(login, "memberId", "tagkdj1");

        when(memberService.loginMember(anyString()))
                .thenReturn(loginDummy);

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(loginDummy.getMemberId()))
                .andExpect(jsonPath("$.memberPwd").value(loginDummy.getMemberPwd()))
                .andExpect(jsonPath("$.memberNo").value(loginDummy.getMemberNo()));
    }

    @Test
    @DisplayName("아이디 중복체크 요청의 결과값 반환")
    void idDuplicateCheckTest() throws Exception {
        IdRequestDto idRequestDto = new IdRequestDto();
        ReflectionTestUtils.setField(idRequestDto, "id", "tagkdj1");

        when(memberService.idDuplicateCheck(anyString()))
                .thenReturn(true);

        mvc.perform(post("/api/signup/idCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("닉네임 중복체크 요청의 결과값 반환")
    void nickDuplicateCheckTest() throws Exception {
        NickRequestDto nickRequestDto = new NickRequestDto();
        ReflectionTestUtils.setField(nickRequestDto, "nickname", "taewon");

        when(memberService.nickNameDuplicateCheck(anyString()))
                .thenReturn(true);

        mvc.perform(post("/api/signup/nickCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @DisplayName("휴대전화를 입력하지 않았을경우")
    @Test
    void memberModifyPhoneValidationFailTest() throws Exception {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", null);

        doNothing().when(memberService)
                .modifyMemberPhone(anyLong(), any());

        mvc.perform(put("/api/members/{memberNo}/phone", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("빈값은 들어갈수없습니다."))
                .andDo(print());
    }

    @DisplayName("휴대전화 양식이 맞지않을경우")
    @Test
    void memberModifyPhoneValidationLengthFailTest() throws Exception {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "111");

        doNothing().when(memberService)
                .modifyMemberPhone(anyLong(), any());
        mvc.perform(put("/api/members/{memberNo}/phone", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("전화번호는 숫자 11글자로 입력해주세요."))
                .andDo(print());
    }

    @DisplayName("휴대전화 변경 성공")
    @Test
    void memberModifyPhoneTest() throws Exception {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "01066749927");

        doNothing().when(memberService)
                .modifyMemberPhone(anyLong(), any());
        mvc.perform(put("/api/members/{memberNo}/phone", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @DisplayName("회원 이름 변경 실패 null")
    @Test
    void memberModifyNameTestNotNull() throws Exception {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "a");

        doNothing().when(memberService)
                .modifyMemberName(anyLong(), any());
        mvc.perform(put("/api/members/{memberNo}/name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("이름은 한글 또는 영어 2글자 이상 200글자 이하로 입력해주세요."));
    }

    @DisplayName("회원 이름 변경 성공")
    @Test
    void memberModifyNameTestSuccess() throws Exception {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "hi");

        doNothing().when(memberService)
                .modifyMemberName(anyLong(), any());
        mvc.perform(put("/api/members/{memberNo}/name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void memberDeleteSuccessTest() throws Exception {

        doNothing().when(memberService)
                .deleteMember(anyLong());

        mvc.perform(put("/api/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("패스워드 값 확인")
    @Test
    void passwordCheckSuccessTest() throws Exception {
        MemberPasswordResponseDto dto = new MemberPasswordResponseDto(MemberDummy.dummy(TierDummy.dummy()));
        when(memberService.getMemberPwd(anyLong()))
                .thenReturn(dto);

        mvc.perform(get("/api/members/{memberNo}/password-check", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.password").value(dto.getPassword()));
    }

    @DisplayName("패스워드 변경 Success ")
    @Test
    void modifyPasswordSuccessTest() throws Exception {
        ModifyMemberPasswordRequest dto = new ModifyMemberPasswordRequest();
        ReflectionTestUtils.setField(dto, "password", "asdfsdf");

        doNothing().when(memberService)
                .modifyMemberPassword(anyLong(), any(ModifyMemberPasswordRequest.class));

        mvc.perform(put("/api/members/{memberNo}/password", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @DisplayName("회원 기준주소지 변경 Success")
    @Test
    void memberModifyBaseAddressTest() throws Exception {

        doNothing().when(memberService)
                .modifyMemberBaseAddress(anyLong(), anyLong());

        mvc.perform(put("/api/members/{memberNo}/addresses/{addressNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(memberService).should().modifyMemberBaseAddress(1L, 1L);
    }

    @DisplayName("회원 주소 등록 테스트 address- validation 실패")
    @Test
    void memberAddressCreateFailTest() throws Exception {

        doNothing().when(memberService).addMemberAddress(anyLong(), any(CreateAddressRequestDto.class));

        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(createAddressRequestDto, "addressDetail", "asdf");

        mvc.perform(post("/api/members/{memberNo}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddressRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("주소값은 비어있을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("회원 주소 등록 테스트 addressdetail- validation 실패")
    @Test
    void memberAddressCreateFail2Test() throws Exception {

        doNothing().when(memberService).addMemberAddress(anyLong(), any(CreateAddressRequestDto.class));

        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(createAddressRequestDto, "address", "aaaa");

        mvc.perform(post("/api/members/{memberNo}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddressRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("상세주소는 비어있을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("회원 주소 등록 테스트 성공")
    @Test
    void memberAddressCreateSuccessTest() throws Exception {

        doNothing().when(memberService).addMemberAddress(anyLong(), any(CreateAddressRequestDto.class));

        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(createAddressRequestDto, "address", "aaaa");
        ReflectionTestUtils.setField(createAddressRequestDto, "addressDetail", "aaaa");

        mvc.perform(post("/api/members/{memberNo}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddressRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

    }

    @SneakyThrows
    @DisplayName("회원 주소 삭제 테스트 성공")
    @Test
    void memberAddressDeleteTest() {
        doNothing().when(memberService).deleteMemberAddress(anyLong(), anyLong());

        mvc.perform(delete("/api/members/{memberNo}/addresses/{addressNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(memberService).should().deleteMemberAddress(1L, 1L);
    }


    @Test
    @DisplayName("oauth 멤버 생성완료")
    void oauthMemberCreateSuccess() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "광주");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);

        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.memberId").value("tagkdj1@github.com"))
                .andExpect(jsonPath("$.memberNickname").value("taewon"))
                .andExpect(jsonPath("$.memberEmail").value("tagkdj1@naver.com"))
                .andExpect(jsonPath("$.tierName").value("basic"));
    }

    @Test
    @DisplayName("oauth 이름 validation 에러로 인한 실패")
    void oauthMemberCreateFailedBecauseNameValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "임");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "광주");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);
        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("이름은 한글 또는 영어 2글자 이상 200글자 이하로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("oauth 닉네임 validation 에러로 인한 실패")
    void oauthMemberCreateFailedBecauseNicknameValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "Abc1123Azxz");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "광주");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);
        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("닉네임은 영어나 숫자로 2글자 이상 8글자 이하로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("oauth 성별 validation 에러로 인한 실패")
    void oauthMemberCreateFailedBecauseGenderValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "마법의 성");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "광주");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);
        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("성별의 길이는 2글자로 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("oauth 생일 validation 에러로 인한 실패")
    void oauthMemberCreateFailedBecauseBirthValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "임태원");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "19981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "광주");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);

        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("생년월일은 숫자로 6글자 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("oauth로 로그인 한 유저의 정보가 자사 db에 있는지 확인해 성공하는 테스트")
    void oauthMemberExistBookpubDb() throws Exception {
        when(memberService.isOauthMember(anyString())).thenReturn(true);

        mvc.perform(get("/api/oauth/{email}","tagkdj1@kakao.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("oauth로 로그인 한 유저의 정보가 자사 db에 있는지 확인해 실패하는 테스트")
    void oauthMemberNotExistBookpubDb() throws Exception {
        when(memberService.isOauthMember(anyString())).thenReturn(false);

        mvc.perform(get("/api/oauth/{email}","tagkdj1@kakao.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));
    }
}