package com.nhnacademy.bookpubshop.inquiry.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dummy.InquiryDummy;
import com.nhnacademy.bookpubshop.inquiry.service.InquiryService;
import java.util.List;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 문의 컨트롤러 테스트
 *
 * @author : 유호철
 * @since : 1.0
 **/
@WebMvcTest(InquiryController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class InquiryControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    InquiryService inquiryService;
    ObjectMapper objectMapper;
    String token = "/token/inquires";
    CreateInquiryRequestDto createInquiryRequestDto;
    MockMultipartFile multipartFile;
    PageRequest pageRequest;
    GetInquirySummaryProductResponseDto getInquirySummaryProductResponseDto;
    GetInquirySummaryResponseDto getInquirySummaryResponseDto;
    GetInquirySummaryMemberResponseDto getInquirySummaryMemberResponseDto;
    GetInquiryResponseDto getInquiryResponseDto;

    @BeforeEach
    void setUp() {
        getInquiryResponseDto = InquiryDummy.getInquiryDummy();
        getInquirySummaryMemberResponseDto = InquiryDummy.getMemberDummy();
        getInquirySummaryResponseDto = InquiryDummy.getErrorInquiryDummy();
        getInquirySummaryProductResponseDto = InquiryDummy.getSummaryDummy();
        pageRequest = PageRequest.of(0, 10);
        createInquiryRequestDto = new CreateInquiryRequestDto();
        objectMapper = new ObjectMapper();
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

    }

    @DisplayName("회원의 구매이력 여부 확인")
    @Test
    void writableInquiryVerify() throws Exception {
        when(inquiryService.verifyWritableInquiry(anyLong(), anyLong()))
                .thenReturn(true);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/inquiries/members/{memberNo}/verify", 1L)
                        .param("productNo", objectMapper.writeValueAsString(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("inquiry-verify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        requestParameters(
                                parameterWithName("productNo").description("상품번호가 기입됩니다.")
                        ),
                        responseBody(
                        )));

        verify(inquiryService, times(1))
                .verifyWritableInquiry(1L, 1L);
    }

    @DisplayName("상품문의 등록 : 상품 번호 미입력")
    @Test
    void inquiryAddFail1() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", null);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", "content");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", false);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("inquiry-add-fail-productNo-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품번호를 입력해주세요.")
                        )));
    }

    @DisplayName("상품문의 등록 : 문의코드번ㅣ 미입력")
    @Test
    void inquiryAddFail2() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", null);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", "content");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", false);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("inquiry-add-fail-inquiryStateCodeNo-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("문의코드번호를 입호력해주세요.")
                        )));
    }

    @DisplayName("상품문의 등록 : 문의제목 입력하지않을경우")
    @Test
    void inquiryAddFail3() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", null);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", "content");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", false);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("inquiry-add-fail-inquiry-title-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("문의 제목을 입력해주세요.")
                        )));
    }

    @DisplayName("상품문의 등록 : 문의제목 입력하지않을경우")
    @Test
    void inquiryAddFail4() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "sadfasdfa" +
                "sfasdfsafsdafassadfasdfasfasdfsafsdafassadfasdfasfasdfsafsd" +
                "afassadfasdfasfasdfsafsdafassadfasdfasfasdfsafsdafassadfasd" +
                "fasfasdfsafsdafassadfasdfasfasdfsafsdafassadfasdfasfasdfsafs" +
                "dafassadfasdfasfasdfsafsdafassadfasdfasfasdfsafsdafassadfasd" +
                "fasfasdfsafsdafassadfasdfasfasdfsafsdafassadfasdfasfasdfsafs" +
                "afassadfasdfasfasd" +
                "fsafsdafassadfasdfasfasdfsafsdafassadfa" +
                "sdfasfasdfsafsdafassadfasdfasfasdfsafsdafas");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", "content");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", false);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("inquiry-add-fail-inquiry-title-long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("문의 제목은 최대 50자입니다.")
                        )));
    }

    @DisplayName("상품문의 등록 : 문의내용이 Null 경우")
    @Test
    void inquiryAddFail5() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", null);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", false);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("inquiry-add-fail-content-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("문의 내용을 입력해주세요.")
                        )));
    }

    @DisplayName("상품문의 등록 : 문의내용이 긴 경우")
    @Test
    void inquiryAddFail6() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent",
                "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다..집가고싶다.." +
                        "");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", false);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("inquiry-add-fail-content-long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("문의 내용은 최대 1000자입니다.")
                        )));
    }

    @DisplayName("상품문의 등록")
    @Test
    void inquiryAddFailSuccess() throws Exception {
        doNothing().when(inquiryService)
                .createInquiry(anyLong(), any());

        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", "content");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", true);

        mvc.perform(RestDocumentationRequestBuilders.post("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInquiryRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("inquiry-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("inquiryParentNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("inquiryStateCodeNo").description("문의상태가 기입됩니다."),
                                fieldWithPath("inquiryTitle").description("문의 제목이 기입됩니다."),
                                fieldWithPath("inquiryContent").description("문의 내용이 기입됩니다."),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부가 기입됩니다."),
                                fieldWithPath("imagePaths.[]").description("이미지 정보가기입됩니다.")
                        ),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호가 기입")
                        )));
    }

    @DisplayName("상품문의에 이미지 추가하는 메서드")
    @Test
    void inquiryAddImage() throws Exception {
        when(inquiryService.addInquiryImage(any()))
                .thenReturn("path");

        mvc.perform(multipart("/token/inquiries/members/{memberNo}/image", 1L)
                        .file(multipartFile))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("inquiry-add-image",
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입")
                        )
                ));

    }

    @DisplayName("답변을 삭제하기 위한 메서드 성공")
    @Test
    void inquiryAnswerCancel() throws Exception {
        doNothing().when(inquiryService)
                .deleteInquiryAnswer(anyLong());

        mvc.perform(RestDocumentationRequestBuilders.delete("/token/inquiries/{inquiryNo}/answer", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("inquiry-delete-answer",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("inquiryNo").description("문의 번호가 기입")
                        )
                ));
    }

    @DisplayName("문의를 삭제하기 위한 테스트")
    @Test
    void inquiryCancel() throws Exception {
        doNothing().when(inquiryService)
                .deleteInquiry(anyLong());

        mvc.perform(RestDocumentationRequestBuilders.delete("/token/inquiries/{inquiryNo}/members/{memberNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(
                        document("inquiry-delete",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("inquiryNo").description("문의 번호가 기입"),
                                        parameterWithName("memberNo").description("회원 번호가 기입")
                                )
                        )
                );
    }

    @DisplayName("답변 여부 수정을 위한 테스트")
    @Test
    void inquiryComplete() throws Exception {
        doNothing().when(inquiryService)
                .modifyCompleteInquiry(anyLong());

        mvc.perform(RestDocumentationRequestBuilders.put("/token/inquiries/{inquiryNo}/complete", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(
                        document("inquiry-modify",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("inquiryNo").description("문의 번호가 기입")
                                )
                        )
                );
    }

    @DisplayName("상품문의 요약 정보를 조회")
    @Test
    void productInquiryList() throws Exception {
        PageImpl<GetInquirySummaryProductResponseDto> page = new PageImpl<>(List.of(getInquirySummaryProductResponseDto));

        when(inquiryService.getSummaryInquiriesByProduct(any(), any()))
                .thenReturn(page);

        mvc.perform(RestDocumentationRequestBuilders.get("/api/inquiries/products/{productNo}", 1L)
                        .param("page", objectMapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageRequest.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[0].inquiryNo").value(objectMapper.writeValueAsString(getInquirySummaryProductResponseDto.getInquiryNo())))
                .andExpect(jsonPath("$.content[0].productNo").value(objectMapper.writeValueAsString(getInquirySummaryProductResponseDto.getProductNo())))
                .andExpect(jsonPath("$.content[0].memberNo").value(objectMapper.writeValueAsString(getInquirySummaryProductResponseDto.getMemberNo())))
                .andExpect(jsonPath("$.content[0].inquiryStateCodeName").value((getInquirySummaryProductResponseDto.getInquiryStateCodeName())))
                .andExpect(jsonPath("$.content[0].memberNickname").value((getInquirySummaryProductResponseDto.getMemberNickname())))
                .andExpect(jsonPath("$.content[0].inquiryTitle").value(getInquirySummaryProductResponseDto.getInquiryTitle()))
                .andExpect(jsonPath("$.content[0].inquiryDisplayed").value(getInquirySummaryProductResponseDto.isInquiryDisplayed()))
                .andExpect(jsonPath("$.content[0].inquiryAnswered").value(getInquirySummaryProductResponseDto.isInquiryAnswered()))
                .andDo(print())
                .andDo(document("inquiry-get-productNo",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호가 들어갑니다.")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 정보 기입"),
                                parameterWithName("size").description("페이지 사이즈 기입")
                        ),
                        responseFields(
                                fieldWithPath("content[].inquiryNo").description("문의번호가 기입됩니다."),
                                fieldWithPath("content[].productNo").description("상품번호가 기입됩니다."),
                                fieldWithPath("content[].memberNo").description("회원번호가 기입됩니다."),
                                fieldWithPath("content[].inquiryStateCodeName").description("문의상태코드가 기입"),
                                fieldWithPath("content[].memberNickname").description("회원의 닉네임이 기입"),
                                fieldWithPath("content[].inquiryTitle").description("문의 타이틀이 기입"),
                                fieldWithPath("content[].inquiryDisplayed").description("문의 공개여부 기입"),
                                fieldWithPath("content[].inquiryAnswered").description("문의 답변여부 기입"),
                                fieldWithPath("content[].createdAt").description("생성일시 기입"),
                                fieldWithPath("number").description("현재 페이지 반환"),
                                fieldWithPath("previous").description("이전 페이지 여부"),
                                fieldWithPath("next").description("다음 페이지 여부"),
                                fieldWithPath("totalPages").description("총 페이지 수 반환")
                        )
                ));

    }

    @DisplayName("관리자가 문의들을 받기위한 메서드 테스트")
    @Test
    void inquiryList() throws Exception {
        PageImpl<GetInquirySummaryResponseDto> page = new PageImpl<>(List.of(getInquirySummaryResponseDto));
        when(inquiryService.getSummaryInquiries(any(), any(), any(), any(), any()))
                .thenReturn(page);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", objectMapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageRequest.getPageSize()))
                        .param("searchKeyFir", "key")
                        .param("searchValueFir", "key")
                        .param("searchKeySec", "key")
                        .param("searchValueSec", "key"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[0].inquiryNo").value(objectMapper.writeValueAsString(getInquirySummaryResponseDto.getInquiryNo())))
                .andExpect(jsonPath("$.content[0].productNo").value(objectMapper.writeValueAsString(getInquirySummaryResponseDto.getProductNo())))
                .andExpect(jsonPath("$.content[0].memberNo").value(objectMapper.writeValueAsString(getInquirySummaryResponseDto.getMemberNo())))
                .andExpect(jsonPath("$.content[0].inquiryStateCodeName").value((getInquirySummaryResponseDto.getInquiryStateCodeName())))
                .andExpect(jsonPath("$.content[0].memberNickname").value((getInquirySummaryResponseDto.getMemberNickname())))
                .andExpect(jsonPath("$.content[0].inquiryTitle").value(getInquirySummaryResponseDto.getInquiryTitle()))
                .andExpect(jsonPath("$.content[0].inquiryDisplayed").value(getInquirySummaryResponseDto.isInquiryDisplayed()))
                .andExpect(jsonPath("$.content[0].inquiryAnswered").value(getInquirySummaryResponseDto.isInquiryAnswered()))
                .andDo(print())
                .andDo(
                        document("inquiry-get-admin",
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("page").description("페이지 정보 기입"),
                                        parameterWithName("size").description("페이지 사이즈 기입"),
                                        parameterWithName("searchValueFir").description("검색 값"),
                                        parameterWithName("searchKeyFir").description("검색 조건 키"),
                                        parameterWithName("searchKeySec").description("검색 조건키 2"),
                                        parameterWithName("searchValueSec").description("검색 값 2")
                                ),
                                responseFields(
                                        fieldWithPath("content[].inquiryNo").description("문의번호가 반환."),
                                        fieldWithPath("content[].productNo").description("상품번호가 반환."),
                                        fieldWithPath("content[].memberNo").description("회원번호가 반환."),
                                        fieldWithPath("content[].inquiryStateCodeName").description("문의상태코드가 반환"),
                                        fieldWithPath("content[].memberNickname").description("회원의 닉네임이 반환"),
                                        fieldWithPath("content[].inquiryTitle").description("문의 타이틀이 반환"),
                                        fieldWithPath("content[].inquiryDisplayed").description("문의 공개여부 반환"),
                                        fieldWithPath("content[].inquiryAnswered").description("문의 답변여부 반환"),
                                        fieldWithPath("content[].createdAt").description("생성일시 반환"),
                                        fieldWithPath("content[].productTitle").description("상품명 반환"),
                                        fieldWithPath("number").description("현재 페이지 반환"),
                                        fieldWithPath("previous").description("이전 페이지 여부"),
                                        fieldWithPath("next").description("다음 페이지 여부"),
                                        fieldWithPath("totalPages").description("총 페이지 수 반환")
                                )
                        )
                );
    }

    @DisplayName("불량상품 문의를 조회")
    @Test
    void errorInquiryList() throws Exception {
        when(inquiryService.getSummaryErrorInquiries(pageRequest))
                .thenReturn(new PageImpl<>(List.of(getInquirySummaryResponseDto)));

        mvc.perform(RestDocumentationRequestBuilders.get("/token/inquiries/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", objectMapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageRequest.getPageSize())))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].inquiryNo").value(objectMapper.writeValueAsString(getInquirySummaryResponseDto.getInquiryNo())))
                .andExpect(jsonPath("$.content[0].productNo").value(objectMapper.writeValueAsString(getInquirySummaryResponseDto.getProductNo())))
                .andExpect(jsonPath("$.content[0].memberNo").value(objectMapper.writeValueAsString(getInquirySummaryResponseDto.getMemberNo())))
                .andExpect(jsonPath("$.content[0].inquiryStateCodeName").value((getInquirySummaryResponseDto.getInquiryStateCodeName())))
                .andExpect(jsonPath("$.content[0].memberNickname").value((getInquirySummaryResponseDto.getMemberNickname())))
                .andExpect(jsonPath("$.content[0].inquiryTitle").value(getInquirySummaryResponseDto.getInquiryTitle()))
                .andExpect(jsonPath("$.content[0].inquiryDisplayed").value(getInquirySummaryResponseDto.isInquiryDisplayed()))
                .andExpect(jsonPath("$.content[0].inquiryAnswered").value(getInquirySummaryResponseDto.isInquiryAnswered()))
                .andDo(print())
                .andDo(
                        document("inquiry-get-error",
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("page").description("페이지 정보 기입"),
                                        parameterWithName("size").description("페이지 사이즈 기입")
                                ),
                                responseFields(
                                        fieldWithPath("content[].inquiryNo").description("문의번호가 반환."),
                                        fieldWithPath("content[].productNo").description("상품번호가 반환."),
                                        fieldWithPath("content[].memberNo").description("회원번호가 반환."),
                                        fieldWithPath("content[].inquiryStateCodeName").description("문의상태코드가 반환"),
                                        fieldWithPath("content[].memberNickname").description("회원의 닉네임이 반환"),
                                        fieldWithPath("content[].inquiryTitle").description("문의 타이틀이 반환"),
                                        fieldWithPath("content[].inquiryDisplayed").description("문의 공개여부 반환"),
                                        fieldWithPath("content[].inquiryAnswered").description("문의 답변여부 반환"),
                                        fieldWithPath("content[].createdAt").description("생성일시 반환"),
                                        fieldWithPath("content[].productTitle").description("상품명 반환"),
                                        fieldWithPath("number").description("현재 페이지 반환"),
                                        fieldWithPath("previous").description("이전 페이지 여부"),
                                        fieldWithPath("next").description("다음 페이지 여부"),
                                        fieldWithPath("totalPages").description("총 페이지 수 반환")
                                )
                        )
                );
    }

    @DisplayName("회원의 상품리스트 조회")
    @Test
    void memberInquiryList() throws Exception {
        PageImpl<GetInquirySummaryMemberResponseDto> page = new PageImpl<>(List.of(getInquirySummaryMemberResponseDto));
        when(inquiryService.getMemberInquiries(any(), anyLong()))
                .thenReturn(page);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", objectMapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageRequest.getPageSize())))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].inquiryNo").value(objectMapper.writeValueAsString(getInquirySummaryMemberResponseDto.getInquiryNo())))
                .andExpect(jsonPath("$.content[0].productNo").value(objectMapper.writeValueAsString(getInquirySummaryMemberResponseDto.getProductNo())))
                .andExpect(jsonPath("$.content[0].memberNo").value(objectMapper.writeValueAsString(getInquirySummaryMemberResponseDto.getMemberNo())))
                .andExpect(jsonPath("$.content[0].inquiryStateCodeName").value((getInquirySummaryMemberResponseDto.getInquiryStateCodeName())))
                .andExpect(jsonPath("$.content[0].productImagePath").value((getInquirySummaryMemberResponseDto.getProductImagePath())))
                .andExpect(jsonPath("$.content[0].inquiryTitle").value(getInquirySummaryMemberResponseDto.getInquiryTitle()))
                .andExpect(jsonPath("$.content[0].productImagePath").value(getInquirySummaryMemberResponseDto.getProductImagePath()))
                .andExpect(jsonPath("$.content[0].inquiryDisplayed").value(getInquirySummaryMemberResponseDto.isInquiryDisplayed()))
                .andExpect(jsonPath("$.content[0].inquiryAnswered").value(getInquirySummaryMemberResponseDto.isInquiryAnswered()))
                .andDo(print())
                .andDo(
                        document("inquiry-get-member",
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("page").description("페이지 정보 기입"),
                                        parameterWithName("size").description("페이지 사이즈 기입")
                                ),
                                responseFields(
                                        fieldWithPath("content[].inquiryNo").description("문의번호가 반환."),
                                        fieldWithPath("content[].productNo").description("상품번호가 반환."),
                                        fieldWithPath("content[].memberNo").description("회원번호가 반환."),
                                        fieldWithPath("content[].inquiryStateCodeName").description("문의상태코드가 반환"),
                                        fieldWithPath("content[].inquiryTitle").description("문의 타이틀이 반환"),
                                        fieldWithPath("content[].inquiryDisplayed").description("문의 공개여부 반환"),
                                        fieldWithPath("content[].inquiryAnswered").description("문의 답변여부 반환"),
                                        fieldWithPath("content[].createdAt").description("생성일시 반환"),
                                        fieldWithPath("content[].productTitle").description("상품명 반환"),
                                        fieldWithPath("content[].productImagePath").description("상품이미지 경로반환"),
                                        fieldWithPath("number").description("현재 페이지 반환"),
                                        fieldWithPath("previous").description("이전 페이지 여부"),
                                        fieldWithPath("next").description("다음 페이지 여부"),
                                        fieldWithPath("totalPages").description("총 페이지 수 반환")
                                )
                        )
                );
    }

    @DisplayName("공개된 상품문의 단건 조회")
    @Test
    void publicInquiryDetails() throws Exception {
        when(inquiryService.getInquiry(anyLong()))
                .thenReturn(getInquiryResponseDto);

        mvc.perform(RestDocumentationRequestBuilders.get("/api/inquiries/{inquiryNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("inquiry-get-view-details",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("inquiryNo").description("문의번호가 기입")
                        ),
                        responseFields(
                                fieldWithPath("inquiryNo").description("문의번호가 반환."),
                                fieldWithPath("productNo").description("상품번호가 반환"),
                                fieldWithPath("memberNo").description("회원번호가 반환"),
                                fieldWithPath("inquiryStateCodeName").description("문의상태코드가 반환"),
                                fieldWithPath("memberNickname").description("회원닉네임을 반환"),
                                fieldWithPath("productTitle").description("상품 제목을 반환"),
                                fieldWithPath("inquiryTitle").description("문의 제목을 반환"),
                                fieldWithPath("inquiryContent").description("문의 내용을 반환"),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부 반환"),
                                fieldWithPath("inquiryAnswered").description("문의 답변여부가 반환"),
                                fieldWithPath("childInquiries.[]").description("하위 문의들 반환"),
                                fieldWithPath("createdAt").description("생성일시 반환")

                        )
                ));
    }

    @DisplayName("상품문의 비공개 단건 조회")
    @Test
    void privateInquiryDetails() throws Exception {
        when(inquiryService.getInquiry(anyLong()))
                .thenReturn(getInquiryResponseDto);

        mvc.perform(RestDocumentationRequestBuilders.get("/token/inquiries/{inquiryNo}/members/{memberNo}", 1L,1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("inquiry-get-details",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("inquiryNo").description("문의번호가 기입"),
                                parameterWithName("memberNo").description("회원번호가 기입")
                        ),
                        responseFields(
                                fieldWithPath("inquiryNo").description("문의번호가 반환."),
                                fieldWithPath("productNo").description("상품번호가 반환"),
                                fieldWithPath("memberNo").description("회원번호가 반환"),
                                fieldWithPath("inquiryStateCodeName").description("문의상태코드가 반환"),
                                fieldWithPath("memberNickname").description("회원닉네임을 반환"),
                                fieldWithPath("productTitle").description("상품 제목을 반환"),
                                fieldWithPath("inquiryTitle").description("문의 제목을 반환"),
                                fieldWithPath("inquiryContent").description("문의 내용을 반환"),
                                fieldWithPath("inquiryDisplayed").description("문의 공개여부 반환"),
                                fieldWithPath("inquiryAnswered").description("문의 답변여부가 반환"),
                                fieldWithPath("childInquiries.[]").description("하위 문의들 반환"),
                                fieldWithPath("createdAt").description("생성일시 반환")

                        )
                ));
    }
}