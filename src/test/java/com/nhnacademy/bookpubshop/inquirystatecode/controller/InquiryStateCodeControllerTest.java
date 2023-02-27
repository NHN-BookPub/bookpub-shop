package com.nhnacademy.bookpubshop.inquirystatecode.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import com.nhnacademy.bookpubshop.inquirystatecode.service.InquiryStateCodeService;
import com.nhnacademy.bookpubshop.state.InquiryState;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;

/**
 * 상품문의상태코드 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(InquiryStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class InquiryStateCodeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    InquiryStateCodeService inquiryStateCodeService;

    ObjectMapper objectMapper;
    String uri = "/api/inquiry-state-codes/";

    GetInquiryStateCodeResponseDto memberInquiryStateDto;
    GetInquiryStateCodeResponseDto adminInquiryStateDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        memberInquiryStateDto = new GetInquiryStateCodeResponseDto(
                1, InquiryState.NORMAL.getName());
        adminInquiryStateDto = new GetInquiryStateCodeResponseDto(
                2, InquiryState.ANSWER.getName());
    }

    @Test
    @DisplayName("회원이 사용하는 상품문의상태코드 조회")
    void codeListForMember() throws Exception {
        //when
        when(inquiryStateCodeService.getUsedCodeForMember())
                .thenReturn(List.of(memberInquiryStateDto));

        //then
        mockMvc.perform(get(uri + "/member")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inquiryCodeNo").value(memberInquiryStateDto.getInquiryCodeNo()))
                .andExpect(jsonPath("$[0].inquiryCodeName").value(memberInquiryStateDto.getInquiryCodeName()))
                .andDo(document("inquiry-state-list-member",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].inquiryCodeNo").description("조회된 상품문의상태코드 번호"),
                                fieldWithPath("[].inquiryCodeName").description("조회된 상품문의상태코드 이름")
                                )));
    }

    @Test
    @DisplayName("관리자가 사용하는 상품문의상태코드 조회")
    void codeListForAdmin() throws Exception {
        //when
        when(inquiryStateCodeService.getUsedCodeForAdmin())
                .thenReturn(List.of(adminInquiryStateDto));

        //then
        mockMvc.perform(get(uri + "/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inquiryCodeNo").value(adminInquiryStateDto.getInquiryCodeNo()))
                .andExpect(jsonPath("$[0].inquiryCodeName").value(adminInquiryStateDto.getInquiryCodeName()))
                .andDo(document("inquiry-state-list-admin",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].inquiryCodeNo").description("상품문의상태코드 번호"),
                                fieldWithPath("[].inquiryCodeName").description("상품문의상태코드 이름")
                        )));

    }
}