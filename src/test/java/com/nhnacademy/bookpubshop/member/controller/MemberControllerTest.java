package com.nhnacademy.bookpubshop.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ?????? ???????????? ?????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(MemberController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;
    @MockBean
    MockHttpServletRequest request;

    String path = "/api/signup";
    String oauthPath = "/api/oauth/signup";

    BookPubTier basic;

    SignUpMemberRequestDto signUpMemberRequestDto;
    OauthMemberCreateRequestDto oauthMemberCreateRequestDto;

    SignUpMemberResponseDto signUpMemberResponseDto;
    SignUpMemberResponseDto oauthSignUpMemberResponseDto;

    MemberDetailResponseDto memberDetailResponseDto;
    MemberResponseDto memberResponseDto;

    @BeforeEach
    void setUp() {
        basic = new BookPubTier("basic", 1, 1L, 100L);
        objectMapper = new ObjectMapper();
        signUpMemberRequestDto = new SignUpMemberRequestDto();
        oauthMemberCreateRequestDto = new OauthMemberCreateRequestDto();
        memberDetailResponseDto = new MemberDetailResponseDto();

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

        memberResponseDto = new MemberResponseDto(
                1L, "tier", "id", "nickname", "name",
                "gender", 1999, 1001, "email", 100L,
                false, false, false
        );
    }

    @Test
    @DisplayName("?????? ????????????")
    void memberCreateSuccess() throws Exception {
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

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.memberId").value("tagkdj1"))
                .andExpect(jsonPath("$.memberNickname").value("taewon"))
                .andExpect(jsonPath("$.memberEmail").value("tagkdj1@naver.com"))
                .andExpect(jsonPath("$.tierName").value("basic"))
                .andDo(document("member-create-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("memberNickname").description("?????????"),
                                fieldWithPath("memberEmail").description("?????????"),
                                fieldWithPath("tierName").description("????????????")
                        )));
    }

    @Test
    @DisplayName("?????? validation ????????? ?????? ??????")
    void memberCreateFailedBecauseNameValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "???");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("????????? ?????? ?????? ?????? 2?????? ?????? 200?????? ????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-create-memberName-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("????????? validation ????????? ?????? ??????")
    void memberCreateFailedBecauseNicknameValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "abc123Abc12");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("???????????? ????????? ????????? 2?????? ?????? 8?????? ????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-create-memberNickname-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("?????? validation ????????? ?????? ??????")
    void memberCreateFailedBecauseGenderValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "?????????????????????????????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("????????? ????????? 2????????? ??????????????????"))
                .andDo(print())
                .andDo(document("member-create-gender-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("?????? validation ????????? ?????? ??????")
    void memberCreateFailedBecauseBirthValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "19981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("??????????????? ????????? 6?????? ??????????????????"))
                .andDo(print())
                .andDo(document("member-create-birthDate-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("????????? validation ????????? ?????? ??????")
    void memberCreateFailedBecauseIdValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagk");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("???????????? ????????? ????????? 5???????????? 20????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-create-memberId-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("???????????? validation ????????? ?????? ??????")
    void memberCreateFailedBecausePhoneValidation() throws Exception {
        ReflectionTestUtils.setField(signUpMemberRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "memberId", "tagkdj1");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "pwd", "!@#ASDFSDAGDCGXZV@!#@!");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "phone", "4358-0106");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "address", "??????");
        ReflectionTestUtils.setField(signUpMemberRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(signUpMemberResponseDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(signUpMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("??????????????? ?????? 11????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-create-phoneNumber-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("????????? ????????? Validation error ")
    void memberModifyNickNameValidException() throws Exception {
        ModifyMemberNicknameRequestDto dto = new ModifyMemberNicknameRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", null);

        doNothing().when(memberService)
                .modifyMemberNickName(anyLong(), any());

        mvc.perform(put("/token/members/{memberNo}/nickName", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("???????????? null ??? ??????????????????."))
                .andDo(print())
                .andDo(document("member-modify-memberNickname-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("?????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("????????? ????????? ?????????")
    void memberModifyNickNameNotCompletedExceptionTest() throws Exception {

        ModifyMemberNicknameRequestDto dto = new ModifyMemberNicknameRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "?????????????????????");

        doNothing().when(memberService)
                .modifyMemberNickName(anyLong(), any());

        mvc.perform(put("/token/members/{memberNo}/nickName", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("???????????? ????????? ?????? ????????? ???????????? 2?????? ?????? 8?????? ????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-modify-memberNickname-expression-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("?????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("????????? ???????????? ?????????")
    void memberModifyNickNameNotCompletedTest() throws Exception {

        ModifyMemberNicknameRequestDto dto = new ModifyMemberNicknameRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "asdf");

        doNothing().when(memberService)
                .modifyMemberNickName(1L, dto);

        mvc.perform(put("/token/members/{memberNo}/nickName", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("member-modify-nickname-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("?????????"))));

        then(memberService).should().modifyMemberNickName(anyLong(), any());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void memberModifyEmailNotCompletedTest() throws Exception {
        ModifyMemberEmailRequestDto dto = new ModifyMemberEmailRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a");
        doNothing().when(memberService)
                .modifyMemberEmail(anyLong(), any());

        mvc.perform(put("/token/members/{memberNo}/email", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("????????? ???????????? ?????????????????????."))
                .andDo(print())
                .andDo(document("member-modify-email-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("?????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void memberEmailSuccessTest() throws Exception {

        ModifyMemberEmailRequestDto dto = new ModifyMemberEmailRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        doNothing().when(memberService)
                .modifyMemberEmail(1L, dto);

        mvc.perform(put("/token/members/{memberNo}/email", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("member-modify-email-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("?????????"))));

        then(memberService).should().modifyMemberEmail(anyLong(), any());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void memberDetailsTest() throws Exception {
        MemberDetailResponseDto dto = MemberDummy.memberDetailResponseDummy();
        when(memberService.getMemberDetails(1L))
                .thenReturn(dto);

        mvc.perform(RestDocumentationRequestBuilders.get("/api/members/{memberNo}", 1L)
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
                .andDo(print())
                .andDo(document("member-getMember-success",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        responseFields(
                                fieldWithPath("memberNo").description("?????? ??????"),
                                fieldWithPath("memberName").description("??????"),
                                fieldWithPath("tierName").description("?????? ??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("birthMonth").description("??????"),
                                fieldWithPath("birthYear").description("??????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("point").description("?????????"),
                                fieldWithPath("authorities").description("??????"),
                                fieldWithPath("addresses").description("??????")
                        )));

        then(memberService)
                .should().getMemberDetails(anyLong());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? ?????? ?????????")
    void memberDetailsTokenTest() throws Exception {
        MemberDetailResponseDto dto = MemberDummy.memberDetailResponseDummy();
        when(memberService.getMemberDetails(1L))
                .thenReturn(dto);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/members/{memberNo}", 1L)
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
                .andDo(print())
                .andDo(document("member-getMember-detail-success",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        responseFields(
                                fieldWithPath("memberNo").description("?????? ??????"),
                                fieldWithPath("memberName").description("??????"),
                                fieldWithPath("tierName").description("?????? ??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("birthMonth").description("??????"),
                                fieldWithPath("birthYear").description("??????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("point").description("?????????"),
                                fieldWithPath("authorities").description("??????"),
                                fieldWithPath("addresses").description("??????")
                        )));

        then(memberService)
                .should().getMemberDetails(anyLong());
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ??????????????????.")
    void memberListTest() throws Exception {
        MemberResponseDto dto = new MemberResponseDto(1L, "tier", "id", "nick",
                "name", "gender", 1, 1, "email",
                1L, false, false, false);
        List<MemberResponseDto> content = List.of(dto);
        PageRequest request = PageRequest.of(0, 10);

        PageImpl<MemberResponseDto> page = new PageImpl<>(content, request, 1);

        when(memberService.getMembers(request))
                .thenReturn(page);

        mvc.perform(get("/token/admin/members")
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
                .andDo(print())
                .andDo(document("member-list-success",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")),
                        responseFields(
                                fieldWithPath("content[].memberNo").description("?????? ??????"),
                                fieldWithPath("content[].tier").description("?????? ??????"),
                                fieldWithPath("content[].memberId").description("?????????"),
                                fieldWithPath("content[].nickname").description("?????????"),
                                fieldWithPath("content[].name").description("??????"),
                                fieldWithPath("content[].gender").description("??????"),
                                fieldWithPath("content[].birthYear").description("??????"),
                                fieldWithPath("content[].birthMonth").description("??????"),
                                fieldWithPath("content[].email").description("?????????"),
                                fieldWithPath("content[].point").description("?????????"),
                                fieldWithPath("content[].social").description("????????????"),
                                fieldWithPath("content[].deleted").description("????????????"),
                                fieldWithPath("content[].blocked").description("????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ?????? ?????? ??????")
                        )));

        then(memberService)
                .should()
                .getMembers(any());
    }

    @Test
    @DisplayName("?????? ?????? ??? ????????? ???????????? ????????? ??????")
    void memberBlockSuccessTest() throws Exception {
        doNothing().when(memberService)
                .blockMember(1L);

        mvc.perform(RestDocumentationRequestBuilders.put("/token/admin/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("member-block-success",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????"))
                ));
    }

    @Test
    @DisplayName("????????? ?????? ")
    void memberStatisticsTest() throws Exception {
        MemberStatisticsResponseDto dto = MemberDummy.memberStatisticsDummy();
        when(memberService.getMemberStatistics())
                .thenReturn(dto);

        mvc.perform(get("/token/admin/members/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberCnt").value(dto.getMemberCnt()))
                .andExpect(jsonPath("$.currentMemberCnt").value(dto.getCurrentMemberCnt()))
                .andExpect(jsonPath("$.deleteMemberCnt").value(dto.getDeleteMemberCnt()))
                .andExpect(jsonPath("$.blockMemberCnt").value(dto.getBlockMemberCnt()))
                .andDo(print())
                .andDo(document("member-statistics-success",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("memberCnt").description("??? ?????? ???"),
                                fieldWithPath("currentMemberCnt").description("?????? ?????? ???"),
                                fieldWithPath("deleteMemberCnt").description("?????? ?????? ???"),
                                fieldWithPath("blockMemberCnt").description("?????? ?????? ???")
                        )));

        then(memberService)
                .should().getMemberStatistics();
    }

    @Test
    @DisplayName("?????? ????????? ??????")
    void memberTierStatisticsTest() throws Exception {

        MemberTierStatisticsResponseDto dto = MemberDummy.memberTierStatisticsDummy();
        when(memberService.getTierStatistics())
                .thenReturn(List.of(dto));

        mvc.perform(get("/token/admin/tier/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tierName").value(dto.getTierName()))
                .andExpect(jsonPath("$[0].tierValue").value(objectMapper.writeValueAsString(dto.getTierValue())))
                .andExpect(jsonPath("$[0].tierCnt").value(objectMapper.writeValueAsString(dto.getTierCnt())))
                .andDo(print())
                .andDo(document("member-statistics-success-byTier",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].tierName").description("?????????"),
                                fieldWithPath("[].tierValue").description("?????? ??????"),
                                fieldWithPath("[].tierCnt").description("?????? ?????????")
                        )));

        then(memberService)
                .should().getTierStatistics();
    }

    @Test
    @DisplayName("????????? ????????? ??? ????????? ????????? ??????")
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
                .andExpect(jsonPath("$.memberNo").value(loginDummy.getMemberNo()))
                .andDo(document("member-login-Member-success",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("memberPwd").description("????????????"),
                                fieldWithPath("memberNo").description("?????? ??????"),
                                fieldWithPath("authorities").description("?????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ????????? ??????")
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
    @DisplayName("????????? ???????????? ????????? ????????? ??????")
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

    @DisplayName("??????????????? ???????????? ???????????????")
    @Test
    void memberModifyPhoneValidationFailTest() throws Exception {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", null);

        doNothing().when(memberService)
                .modifyMemberPhone(anyLong(), any());

        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/phone", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("????????? ????????????????????????."))
                .andDo(print())
                .andDo(document("member-modify-phone-null-fail",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("phone").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @DisplayName("???????????? ????????? ??????????????????")
    @Test
    void memberModifyPhoneValidationLengthFailTest() throws Exception {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "111");

        doNothing().when(memberService)
                .modifyMemberPhone(anyLong(), any());
        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/phone", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("??????????????? ?????? 11????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-modify-phone-valid-fail",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("phone").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @DisplayName("???????????? ?????? ??????")
    @Test
    void memberModifyPhoneTest() throws Exception {
        ModifyMemberPhoneRequestDto dto = new ModifyMemberPhoneRequestDto();
        ReflectionTestUtils.setField(dto, "phone", "01066749927");

        doNothing().when(memberService)
                .modifyMemberPhone(anyLong(), any());
        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/phone", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("member-modify-phone-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("phone").description("????????????"))));
    }

    @DisplayName("?????? ?????? ?????? ?????? null")
    @Test
    void memberModifyNameTestNotNull() throws Exception {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "a");

        doNothing().when(memberService)
                .modifyMemberName(anyLong(), any());
        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message")
                        .value("????????? ?????? ?????? ?????? 2?????? ?????? 200?????? ????????? ??????????????????."))
                .andDo(document("member-modify-name-null-fail",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("name").description("??????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @DisplayName("?????? ?????? ?????? ??????")
    @Test
    void memberModifyNameTestSuccess() throws Exception {
        ModifyMemberNameRequestDto dto = new ModifyMemberNameRequestDto();
        ReflectionTestUtils.setField(dto, "name", "hi");

        doNothing().when(memberService)
                .modifyMemberName(anyLong(), any());
        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member-modify-name-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("name").description("??????"))));
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void memberDeleteSuccessTest() throws Exception {

        doNothing().when(memberService)
                .deleteMember(anyLong());

        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member-delete-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????"))));
    }

    @DisplayName("???????????? ??? ??????")
    @Test
    void passwordCheckSuccessTest() throws Exception {
        MemberPasswordResponseDto dto = new MemberPasswordResponseDto(MemberDummy.dummy(TierDummy.dummy()));
        when(memberService.getMemberPwd(anyLong()))
                .thenReturn(dto);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/members/{memberNo}/password-check", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.password").value(dto.getPassword()))
                .andDo(document("password-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        responseFields(
                                fieldWithPath("password").description("????????????"))));
    }

    @DisplayName("???????????? ?????? Success ")
    @Test
    void modifyPasswordSuccessTest() throws Exception {
        ModifyMemberPasswordRequest dto = new ModifyMemberPasswordRequest();
        ReflectionTestUtils.setField(dto, "password", "asdfsdf");

        doNothing().when(memberService)
                .modifyMemberPassword(anyLong(), any(ModifyMemberPasswordRequest.class));

        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/password", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("password-modify-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("password").description("????????????"))));
    }

    @DisplayName("?????? ??????????????? ?????? Success")
    @Test
    void memberModifyBaseAddressTest() throws Exception {

        doNothing().when(memberService)
                .modifyMemberBaseAddress(anyLong(), anyLong());

        mvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/addresses/{addressNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("baseAddress-modify-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????"),
                                parameterWithName("addressNo").description("?????? ??????"))));


        then(memberService).should().modifyMemberBaseAddress(1L, 1L);
    }

    @DisplayName("?????? ?????? ?????? ????????? address- validation ??????")
    @Test
    void memberAddressCreateFailTest() throws Exception {

        doNothing().when(memberService).addMemberAddress(anyLong(), any(CreateAddressRequestDto.class));

        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(createAddressRequestDto, "addressDetail", "asdf");

        mvc.perform(RestDocumentationRequestBuilders.post("/token/members/{memberNo}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddressRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("???????????? ???????????? ??? ????????????."))
                .andDo(print())
                .andDo(document("address-create-valid-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @DisplayName("?????? ?????? ?????? ????????? addressdetail- validation ??????")
    @Test
    void memberAddressCreateFail2Test() throws Exception {

        doNothing().when(memberService).addMemberAddress(anyLong(), any(CreateAddressRequestDto.class));

        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(createAddressRequestDto, "address", "aaaa");

        mvc.perform(RestDocumentationRequestBuilders.post("/token/members/{memberNo}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddressRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("??????????????? ???????????? ??? ????????????."))
                .andDo(print())
                .andDo(document("address-create-valid-detail-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("addressDetail").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @DisplayName("?????? ?????? ?????? ????????? ??????")
    @Test
    void memberAddressCreateSuccessTest() throws Exception {

        doNothing().when(memberService).addMemberAddress(anyLong(), any(CreateAddressRequestDto.class));

        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(createAddressRequestDto, "address", "aaaa");
        ReflectionTestUtils.setField(createAddressRequestDto, "addressDetail", "aaaa");

        mvc.perform(RestDocumentationRequestBuilders.post("/token/members/{memberNo}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddressRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("address-create-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("addressDetail").description("????????????")
                        )));

    }

    @SneakyThrows
    @DisplayName("?????? ?????? ?????? ????????? ??????")
    @Test
    void memberAddressDeleteTest() {
        doNothing().when(memberService).deleteMemberAddress(anyLong(), anyLong());

        mvc.perform(RestDocumentationRequestBuilders.delete("/token/members/{memberNo}/addresses/{addressNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("address-delete-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("?????? ??????"),
                                parameterWithName("addressNo").description("?????? ??????"))));

        then(memberService).should().deleteMemberAddress(1L, 1L);
    }


    @Test
    @DisplayName("oauth ?????? ????????????")
    void oauthMemberCreateSuccess() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);

        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.memberId").value("tagkdj1@github.com"))
                .andExpect(jsonPath("$.memberNickname").value("taewon"))
                .andExpect(jsonPath("$.memberEmail").value("tagkdj1@naver.com"))
                .andExpect(jsonPath("$.tierName").value("basic"))
                .andDo(document("member-oauth-create-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("memberNickname").description("?????????"),
                                fieldWithPath("memberEmail").description("?????????"),
                                fieldWithPath("tierName").description("????????????")
                        )));
    }

    @Test
    @DisplayName("oauth ?????? validation ????????? ?????? ??????")
    void oauthMemberCreateFailedBecauseNameValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "???");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);
        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("????????? ?????? ?????? ?????? 2?????? ?????? 200?????? ????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-oauth-create-name-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("oauth ????????? validation ????????? ?????? ??????")
    void oauthMemberCreateFailedBecauseNicknameValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "Abc1123Azxz");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);
        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("???????????? ????????? ????????? 2?????? ?????? 8?????? ????????? ??????????????????."))
                .andDo(print())
                .andDo(document("member-oauth-create-nickname-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("oauth ?????? validation ????????? ?????? ??????")
    void oauthMemberCreateFailedBecauseGenderValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "????????? ???");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);
        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("????????? ????????? 2????????? ??????????????????"))
                .andDo(print())
                .andDo(document("member-oauth-create-gender-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("oauth ?????? validation ????????? ?????? ??????")
    void oauthMemberCreateFailedBecauseBirthValidation() throws Exception {
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "name", "?????????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "nickname", "taewon");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "birth", "19981008");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "gender", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "memberId", "tagkdj1@github.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "pwd", "github");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "phone", "01043580106");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "email", "tagkdj1@naver.com");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "address", "??????");
        ReflectionTestUtils.setField(oauthMemberCreateRequestDto, "detailAddress", "109??? 102???");

        when(memberService.signup(any())).thenReturn(oauthSignUpMemberResponseDto);

        //when && then
        mvc.perform(post(oauthPath)
                        .content(objectMapper.writeValueAsString(oauthMemberCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("??????????????? ????????? 6?????? ??????????????????"))
                .andDo(print())
                .andDo(document("member-oauth-create-birth-valid-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????"),
                                fieldWithPath("gender").description("??????"),
                                fieldWithPath("memberId").description("?????????"),
                                fieldWithPath("pwd").description("????????????"),
                                fieldWithPath("phone").description("????????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("address").description("????????????"),
                                fieldWithPath("detailAddress").description("????????????")),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ??????"))));
    }

    @Test
    @DisplayName("oauth??? ????????? ??? ????????? ????????? ?????? db??? ????????? ????????? ???????????? ?????????")
    void oauthMemberExistBookpubDb() throws Exception {
        when(memberService.isOauthMember(anyString())).thenReturn(true);

        mvc.perform(get("/api/oauth/{email}", "tagkdj1@kakao.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("oauth??? ????????? ??? ????????? ????????? ?????? db??? ????????? ????????? ???????????? ?????????")
    void oauthMemberNotExistBookpubDb() throws Exception {
        when(memberService.isOauthMember(anyString())).thenReturn(false);

        mvc.perform(get("/api/oauth/{email}", "tagkdj1@kakao.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????")
    void memberListByNickTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<MemberResponseDto> content = List.of(memberResponseDto);
        PageImpl<MemberResponseDto> page = new PageImpl<>(content, pageable, 1);

        when(memberService.getMembersByNickName(any(), anyString()))
                .thenReturn(page);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/admin/members/{search}/nick", "search")
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].memberNo").value(memberResponseDto.getMemberNo()))
                .andExpect(jsonPath("$.content[0].tier").value(memberResponseDto.getTier()))
                .andExpect(jsonPath("$.content[0].memberId").value(memberResponseDto.getMemberId()))
                .andExpect(jsonPath("$.content[0].nickname").value(memberResponseDto.getNickname()))
                .andExpect(jsonPath("$.content[0].name").value(memberResponseDto.getName()))
                .andExpect(jsonPath("$.content[0].gender").value(memberResponseDto.getGender()))
                .andExpect(jsonPath("$.content[0].birthYear").value(memberResponseDto.getBirthYear()))
                .andExpect(jsonPath("$.content[0].birthMonth").value(memberResponseDto.getBirthMonth()))
                .andExpect(jsonPath("$.content[0].email").value(memberResponseDto.getEmail()))
                .andExpect(jsonPath("$.content[0].point").value(memberResponseDto.getPoint()))
                .andExpect(jsonPath("$.content[0].social").value(memberResponseDto.isSocial()))
                .andExpect(jsonPath("$.content[0].deleted").value(memberResponseDto.isDeleted()))
                .andExpect(jsonPath("$.content[0].blocked").value(memberResponseDto.isBlocked()))
                .andDo(document("member-detail-get-by-nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("search").description("????????? ?????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("page").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("content[].memberNo").description("?????? ??????"),
                                fieldWithPath("content[].tier").description("?????? ??????"),
                                fieldWithPath("content[].memberId").description("?????????"),
                                fieldWithPath("content[].nickname").description("?????????"),
                                fieldWithPath("content[].name").description("??????"),
                                fieldWithPath("content[].gender").description("??????"),
                                fieldWithPath("content[].birthYear").description("??????"),
                                fieldWithPath("content[].birthMonth").description("??????"),
                                fieldWithPath("content[].email").description("?????????"),
                                fieldWithPath("content[].point").description("?????????"),
                                fieldWithPath("content[].social").description("????????????"),
                                fieldWithPath("content[].deleted").description("????????????"),
                                fieldWithPath("content[].blocked").description("????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ?????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????????")
    void memberListByIdTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<MemberResponseDto> content = List.of(memberResponseDto);
        PageImpl<MemberResponseDto> page = new PageImpl<>(content, pageable, 1);

        when(memberService.getMembersById(any(), anyString()))
                .thenReturn(page);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/admin/members/{search}/id", "searchId")
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].memberNo").value(memberResponseDto.getMemberNo()))
                .andExpect(jsonPath("$.content[0].tier").value(memberResponseDto.getTier()))
                .andExpect(jsonPath("$.content[0].memberId").value(memberResponseDto.getMemberId()))
                .andExpect(jsonPath("$.content[0].nickname").value(memberResponseDto.getNickname()))
                .andExpect(jsonPath("$.content[0].name").value(memberResponseDto.getName()))
                .andExpect(jsonPath("$.content[0].gender").value(memberResponseDto.getGender()))
                .andExpect(jsonPath("$.content[0].birthYear").value(memberResponseDto.getBirthYear()))
                .andExpect(jsonPath("$.content[0].birthMonth").value(memberResponseDto.getBirthMonth()))
                .andExpect(jsonPath("$.content[0].email").value(memberResponseDto.getEmail()))
                .andExpect(jsonPath("$.content[0].point").value(memberResponseDto.getPoint()))
                .andExpect(jsonPath("$.content[0].social").value(memberResponseDto.isSocial()))
                .andExpect(jsonPath("$.content[0].deleted").value(memberResponseDto.isDeleted()))
                .andExpect(jsonPath("$.content[0].blocked").value(memberResponseDto.isBlocked()))
                .andDo(document("member-detail-get-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("search").description("????????? ?????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("page").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("content[].memberNo").description("?????? ??????"),
                                fieldWithPath("content[].tier").description("?????? ??????"),
                                fieldWithPath("content[].memberId").description("?????????"),
                                fieldWithPath("content[].nickname").description("?????????"),
                                fieldWithPath("content[].name").description("??????"),
                                fieldWithPath("content[].gender").description("??????"),
                                fieldWithPath("content[].birthYear").description("??????"),
                                fieldWithPath("content[].birthMonth").description("??????"),
                                fieldWithPath("content[].email").description("?????????"),
                                fieldWithPath("content[].point").description("?????????"),
                                fieldWithPath("content[].social").description("????????????"),
                                fieldWithPath("content[].deleted").description("????????????"),
                                fieldWithPath("content[].blocked").description("????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ?????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ??????????????? ?????? ?????????")
    void getTierNoByMemberNo() throws Exception {
        when(memberService.getTierByMemberNo(anyLong())).thenReturn(1);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/members/{memberNo}/tier", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber())
                .andDo(print())
                .andDo(document("member-tierNo-get-by-memberNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????? ?????? ??????")
                        ),
                        responseBody(
                                Map.of("tierNo", "?????? ??????")
                        )));
    }
}