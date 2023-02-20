package com.nhnacademy.bookpubshop.personalinquiryanswer.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.personalinquiryanswer.dto.request.CreatePersonalInquiryAnswerRequestDto;
import com.nhnacademy.bookpubshop.personalinquiryanswer.service.PersonalInquiryAnswerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 1:1문의 답변 컨트롤러 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@WebMvcTest(PersonalInquiryAnswerController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PersonalInquiryAnswerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonalInquiryAnswerService personalInquiryAnswerService;

    ObjectMapper mapper;
    CreatePersonalInquiryAnswerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        requestDto = new CreatePersonalInquiryAnswerRequestDto();
    }

    @Test
    @DisplayName("문의 답변 생성 성공")
    void personalInquiryAnswerAdd() throws Exception {
        ReflectionTestUtils.setField(requestDto, "personalInquiryNo", 1L);
        ReflectionTestUtils.setField(requestDto, "personalInquiryAnswerContent", "content");

        doNothing().when(personalInquiryAnswerService).createPersonalInquiryAnswer(any());

        mockMvc.perform(post("/token/personal-inquiry-answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(document("personal-inquiry-answer-create",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("personalInquiryNo").description("문의번호"),
                                fieldWithPath("personalInquiryAnswerContent").description("문의 답변 내용")
                        )));
    }

    @Test
    @DisplayName("문의 답변 생성 실패 - 문의번호 null")
    void personalInquiryAnswerAdd_fail_inquiryNo() throws Exception {
        ReflectionTestUtils.setField(requestDto, "personalInquiryNo", null);
        ReflectionTestUtils.setField(requestDto, "personalInquiryAnswerContent", "content");

        doNothing().when(personalInquiryAnswerService).createPersonalInquiryAnswer(any());

        mockMvc.perform(post("/token/personal-inquiry-answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("1대1문의 질문 번호를 입력해주세요."))
                .andDo(document("personal-inquiry-answer-create-fail-valid-inquiryNo",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("personalInquiryNo").description("문의번호"),
                                fieldWithPath("personalInquiryAnswerContent").description("문의 답변 내용")
                        )));
    }

    @Test
    @DisplayName("문의 답변 생성 실패 - 문의답변 null")
    void personalInquiryAnswerAdd_fail_content() throws Exception {
        ReflectionTestUtils.setField(requestDto, "personalInquiryNo", 1L);
        ReflectionTestUtils.setField(requestDto, "personalInquiryAnswerContent", null);

        doNothing().when(personalInquiryAnswerService).createPersonalInquiryAnswer(any());

        mockMvc.perform(post("/token/personal-inquiry-answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("1대1문의 답변을 입력해주세요."))
                .andDo(document("personal-inquiry-answer-create-fail-valid-content",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("personalInquiryNo").description("문의번호"),
                                fieldWithPath("personalInquiryAnswerContent").description("문의 답변 내용")
                        )));
    }

    @SneakyThrows
    @Test
    @DisplayName("1:1 문의 답변 삭제")
    void personalInquiryAnswerDelete() {
        doNothing().when(personalInquiryAnswerService).deletePersonalInquiryAnswer(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/token/personal-inquiry-answers/{answerNo}", 1L))
                .andExpect(status().isOk())
                .andDo(document("inquiry-answer-delete",
                        pathParameters(
                                parameterWithName("answerNo").description("문의 답변 번호")
                        )));
    }
}