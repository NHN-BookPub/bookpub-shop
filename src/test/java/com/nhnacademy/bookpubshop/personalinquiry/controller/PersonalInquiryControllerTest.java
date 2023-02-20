package com.nhnacademy.bookpubshop.personalinquiry.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.personalinquiry.dto.request.CreatePersonalInquiryRequestDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.service.PersonalInquiryService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 1:1 문의 컨트롤러 테스트.
 * 패
 *
 * @author : 임태원
 * @since : 1.0
 **/
@WebMvcTest(PersonalInquiryController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PersonalInquiryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonalInquiryService personalInquiryService;

    ObjectMapper mapper;
    CreatePersonalInquiryRequestDto requestDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        requestDto = new CreatePersonalInquiryRequestDto();
    }

    @Test
    @DisplayName("회원 문의 생성 성공")
    void personalInquiryAdd() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(document("personal-inquiry-create",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("회원번호"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용")
                        )));
    }

    @Test
    @DisplayName("회원 문의 생성 실패 - 회원번호 null")
    void personalInquiryAdd_fail_memberNo() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", null);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("회원 번호를 입력해주세요."))
                .andDo(document("personal-inquiry-create-fail-valid-member",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("회원번호"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용")
                        ), responseFields(
                                fieldWithPath("[].message").description("실패사유")
                        )));
    }

    @Test
    @DisplayName("회원 문의 생성 실패 - 문의제목 null")
    void personalInquiryAdd_fail_inquiryTitle_null() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", null);
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("문의 제목를 입력해주세요."))
                .andDo(document("personal-inquiry-create-fail-valid-title_null",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("회원번호"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용")
                        ), responseFields(
                                fieldWithPath("[].message").description("실패사유")
                        )));
    }

    @Test
    @DisplayName("회원 문의 생성 실패 - 문의제목 size")
    void personalInquiryAdd_fail_inquiryTitle_size() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "sizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesize");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("문의제목은 100자를 넘길 수 없습니다"))
                .andDo(document("personal-inquiry-create-fail-valid-title_size",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("회원번호"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용")
                        ), responseFields(
                                fieldWithPath("[].message").description("실패사유")
                        )));
    }

    @Test
    @DisplayName("회원 문의 생성 실패 - 문의내용 null")
    void personalInquiryAdd_fail_inquiryContent_null() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", null);

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("문의 내용을 입력해주세요."))
                .andDo(document("personal-inquiry-create-fail-valid-content_null",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("회원번호"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용")
                        ), responseFields(
                                fieldWithPath("[].message").description("실패사유")
                        )));
    }

    @Test
    @DisplayName("회원 문의 생성 실패 - 문의내용 size")
    void personalInquiryAdd_fail_inquiryContent_size() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "sizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesize");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("문의 내용은 2000자를 넘길 수 없습니다"))
                .andDo(document("personal-inquiry-create-fail-valid-content_size",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("회원번호"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용")
                        ), responseFields(
                                fieldWithPath("[].message").description("실패사유")
                        )));
    }

    @Test
    @DisplayName("1:1문의 삭제")
    void personalInquiryDelete() throws Exception {
        doNothing().when(personalInquiryService).deletePersonalInquiry(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/token/personal-inquiries/{personalInquiryNo}/members/{memberNo}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("personal-inquiry-delete",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("personalInquiryNo").description("문의번호"),
                                parameterWithName("memberNo").description("회원번호")
                        )
                ));
    }

    @Test
    @DisplayName("1:1 문의 전체 조회")
    void personalInquiryList() throws Exception {
        GetSimplePersonalInquiryResponseDto dto = new GetSimplePersonalInquiryResponseDto(
                1L,
                "nickname",
                "title",
                true,
                LocalDateTime.of(2023, 2, 19, 0, 0)
        );
        List<GetSimplePersonalInquiryResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetSimplePersonalInquiryResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);

        when(personalInquiryService.getPersonalInquiries(any()))
                .thenReturn(page);

        mockMvc.perform(get("/token/personal-inquiries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].inquiryNo").value(1L))
                .andExpect(jsonPath("$.content[0].memberNickname").value("nickname"))
                .andExpect(jsonPath("$.content[0].inquiryTitle").value("title"))
                .andExpect(jsonPath("$.content[0].inquiryAnswered").value(true))
                .andExpect(jsonPath("$.content[0].createdAt").value("2023-02-19T00:00:00"))
                .andDo(document("personal-inquiries-list",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].inquiryNo").description("문의번호"),
                                fieldWithPath("content[].memberNickname").description("회원 닉네임"),
                                fieldWithPath("content[].inquiryTitle").description("문의 제목"),
                                fieldWithPath("content[].inquiryAnswered").description("답변여부"),
                                fieldWithPath("content[].createdAt").description("문의 생성시간"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 번호"),
                                fieldWithPath("next").description("다음 페이지 번호")
                        )));
    }

    @Test
    @DisplayName("해당 회원의 1대1문의 리스트 조회 테스트")
    void memberPersonalInquiryList() throws Exception {
        GetSimplePersonalInquiryResponseDto dto = new GetSimplePersonalInquiryResponseDto(
                1L,
                "nickname",
                "title",
                true,
                LocalDateTime.of(2023, 2, 19, 0, 0)
        );
        List<GetSimplePersonalInquiryResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetSimplePersonalInquiryResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);

        when(personalInquiryService.getMemberPersonalInquiries(any(), anyLong()))
                .thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/personal-inquiries/members/{memberNo}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].inquiryNo").value(1L))
                .andExpect(jsonPath("$.content[0].memberNickname").value("nickname"))
                .andExpect(jsonPath("$.content[0].inquiryTitle").value("title"))
                .andExpect(jsonPath("$.content[0].inquiryAnswered").value(true))
                .andExpect(jsonPath("$.content[0].createdAt").value("2023-02-19T00:00:00"))
                .andDo(document("personal-inquiries-list-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("content[].inquiryNo").description("문의번호"),
                                fieldWithPath("content[].memberNickname").description("회원 닉네임"),
                                fieldWithPath("content[].inquiryTitle").description("문의 제목"),
                                fieldWithPath("content[].inquiryAnswered").description("답변여부"),
                                fieldWithPath("content[].createdAt").description("문의 생성시간"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 번호"),
                                fieldWithPath("next").description("다음 페이지 번호")
                        )));

    }

    @Test
    @DisplayName("1대1문의 단건 상세 조회 메소드")
    void personalInquiryDetail() throws Exception {
        GetPersonalInquiryResponseDto tmp = new GetPersonalInquiryResponseDto();
        GetPersonalInquiryResponseDto dto = new GetPersonalInquiryResponseDto(
                1L,
                "nickname",
                "title",
                "content",
                true,
                LocalDateTime.of(2023, 2, 19, 0, 0),
                1L,
                "answerContent",
                LocalDateTime.of(2023, 2, 19, 0, 0)
        );

        when(personalInquiryService.getPersonalInquiry(anyLong()))
                .thenReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/personal-inquiries/{inquiryNo}/members/{memberNo}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.inquiryNo").value(1L))
                .andExpect(jsonPath("$.memberNickname").value("nickname"))
                .andExpect(jsonPath("$.inquiryTitle").value("title"))
                .andExpect(jsonPath("$.inquiryContent").value("content"))
                .andExpect(jsonPath("$.inquiryAnswered").value(true))
                .andExpect(jsonPath("$.createdAt").value("2023-02-19T00:00:00"))
                .andExpect(jsonPath("$.inquiryAnswerNo").value(1L))
                .andExpect(jsonPath("$.inquiryAnswerContent").value("answerContent"))
                .andExpect(jsonPath("$.answerCreatedAt").value("2023-02-19T00:00:00"))
                .andDo(document("personal-inquiry-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("inquiryNo").description("문의번호"),
                                parameterWithName("memberNo").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("inquiryNo").description("문의번호"),
                                fieldWithPath("memberNickname").description("회원 닉네임"),
                                fieldWithPath("inquiryTitle").description("문의 제목"),
                                fieldWithPath("inquiryContent").description("문의 내용"),
                                fieldWithPath("inquiryAnswered").description("답변여부"),
                                fieldWithPath("createdAt").description("문의 생성시간"),
                                fieldWithPath("inquiryAnswerNo").description("문의 답변 번호"),
                                fieldWithPath("inquiryAnswerContent").description("문의 답변 내용"),
                                fieldWithPath("answerCreatedAt").description("문의 답변 생성 시간")
                        )));

    }
}