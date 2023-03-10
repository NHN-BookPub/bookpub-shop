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
 * 1:1 ?????? ???????????? ?????????.
 * ???
 *
 * @author : ?????????
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
    @DisplayName("?????? ?????? ?????? ??????")
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
                                parameterWithName("memberNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ???????????? null")
    void personalInquiryAdd_fail_memberNo() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", null);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("?????? ????????? ??????????????????."))
                .andDo(document("personal-inquiry-create-fail-valid-member",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????")
                        ), responseFields(
                                fieldWithPath("[].message").description("????????????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ???????????? null")
    void personalInquiryAdd_fail_inquiryTitle_null() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", null);
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("?????? ????????? ??????????????????."))
                .andDo(document("personal-inquiry-create-fail-valid-title_null",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????")
                        ), responseFields(
                                fieldWithPath("[].message").description("????????????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ???????????? size")
    void personalInquiryAdd_fail_inquiryTitle_size() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "sizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesize");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("??????????????? 100?????? ?????? ??? ????????????"))
                .andDo(document("personal-inquiry-create-fail-valid-title_size",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????")
                        ), responseFields(
                                fieldWithPath("[].message").description("????????????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ???????????? null")
    void personalInquiryAdd_fail_inquiryContent_null() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", null);

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("?????? ????????? ??????????????????."))
                .andDo(document("personal-inquiry-create-fail-valid-content_null",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????")
                        ), responseFields(
                                fieldWithPath("[].message").description("????????????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ???????????? size")
    void personalInquiryAdd_fail_inquiryContent_size() throws Exception {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "sizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesizesize");

        doNothing().when(personalInquiryService).createPersonalInquiry(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/personal-inquiries/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("?????? ????????? 2000?????? ?????? ??? ????????????"))
                .andDo(document("personal-inquiry-create-fail-valid-content_size",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????")
                        ), responseFields(
                                fieldWithPath("[].message").description("????????????")
                        )));
    }

    @Test
    @DisplayName("1:1?????? ??????")
    void personalInquiryDelete() throws Exception {
        doNothing().when(personalInquiryService).deletePersonalInquiry(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/token/personal-inquiries/{personalInquiryNo}/members/{memberNo}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("personal-inquiry-delete",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("personalInquiryNo").description("????????????"),
                                parameterWithName("memberNo").description("????????????")
                        )
                ));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ??????")
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
                                fieldWithPath("content[].inquiryNo").description("????????????"),
                                fieldWithPath("content[].memberNickname").description("?????? ?????????"),
                                fieldWithPath("content[].inquiryTitle").description("?????? ??????"),
                                fieldWithPath("content[].inquiryAnswered").description("????????????"),
                                fieldWithPath("content[].createdAt").description("?????? ????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? 1???1?????? ????????? ?????? ?????????")
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
                                parameterWithName("memberNo").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("content[].inquiryNo").description("????????????"),
                                fieldWithPath("content[].memberNickname").description("?????? ?????????"),
                                fieldWithPath("content[].inquiryTitle").description("?????? ??????"),
                                fieldWithPath("content[].inquiryAnswered").description("????????????"),
                                fieldWithPath("content[].createdAt").description("?????? ????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ??????")
                        )));

    }

    @Test
    @DisplayName("1???1?????? ?????? ?????? ?????? ?????????")
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
                                parameterWithName("inquiryNo").description("????????????"),
                                parameterWithName("memberNo").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("inquiryNo").description("????????????"),
                                fieldWithPath("memberNickname").description("?????? ?????????"),
                                fieldWithPath("inquiryTitle").description("?????? ??????"),
                                fieldWithPath("inquiryContent").description("?????? ??????"),
                                fieldWithPath("inquiryAnswered").description("????????????"),
                                fieldWithPath("createdAt").description("?????? ????????????"),
                                fieldWithPath("inquiryAnswerNo").description("?????? ?????? ??????"),
                                fieldWithPath("inquiryAnswerContent").description("?????? ?????? ??????"),
                                fieldWithPath("answerCreatedAt").description("?????? ?????? ?????? ??????")
                        )));

    }
}