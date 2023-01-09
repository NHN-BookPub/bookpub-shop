package com.nhnacademy.bookpubshop.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.member.dto.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    String path = "/api/signup";

    BookPubTier basic;

    SignUpMemberRequestDto signUpMemberRequestDto;

    @BeforeEach
    void setUp() {
        basic = new BookPubTier("basic");
        objectMapper = new ObjectMapper();
        signUpMemberRequestDto = new SignUpMemberRequestDto();
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

        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(basic)
        );

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

        basic = new BookPubTier(null, "basic");
        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(basic)
        );

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
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "123123");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "남성");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(basic)
        );

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("닉네임은 영어는 필수 숫자는 선택으로 2글자 이상 8글자 이하로 입력해주세요."))
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

        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(basic)
        );

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

        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(basic)
        );

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
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "광주");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109동 102호");

        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(new BookPubTier(null, "basic"))
        );

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("아이디는 영어와 숫자를 섞어 5글자에서 20글자로 입력해주세요."))
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

        when(memberService.signup(any(), anyString())).thenReturn(
                signUpMemberRequestDto.createMember(basic)
        );

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("전화번호는 숫자 11글자로 입력해주세요."))
                .andDo(print());
    }

}