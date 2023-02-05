package com.nhnacademy.bookpubshop.couponstatecode.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import com.nhnacademy.bookpubshop.couponstatecode.service.CouponStateCodeService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 쿠폰상태코드 Rest 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponStateCodeRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CouponStateCodeRestControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponStateCodeService couponStateCodeService;

    @Test
    @DisplayName("사용 쿠폰상태코드 가져오기")
    void getCouponStateCode() throws Exception {
        GetCouponStateCodeResponseDto dto = new GetCouponStateCodeResponseDto(1, "test_target");
        given(couponStateCodeService.getCouponStateCode(anyInt()))
                .willReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/coupon-state-codes/{codeNo}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.codeNo", equalTo(dto.getCodeNo())))
                .andExpect(jsonPath("$.codeTarget", equalTo(dto.getCodeTarget())))
                .andDo(document("coupon-state-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("codeNo").description("Path 로 상태코드 번호 기입")),
                        responseFields(
                                fieldWithPath("codeNo").description("쿠폰 상태코드 번호"),
                                fieldWithPath("codeTarget").description("적용 대상")
                        )
                ));

        verify(couponStateCodeService).getCouponStateCode(1);
    }

    @Test
    @DisplayName("사용 쿠폰상태코드 리스트 가져오기")
    void getCouponStateCodes() throws Exception {
        List<GetCouponStateCodeResponseDto> dto = List.of(
                new GetCouponStateCodeResponseDto(1, "test_target_one"),
                new GetCouponStateCodeResponseDto(2, "test_target_two")
        );
        given(couponStateCodeService.getCouponStateCodes())
                .willReturn(dto);

        mockMvc.perform(get("/token/coupon-state-codes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].codeNo", equalTo(dto.get(0).getCodeNo())))
                .andExpect(jsonPath("$[0].codeTarget", equalTo(dto.get(0).getCodeTarget())))
                .andDo(document("coupon-state-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("쿠폰 상태코드 번호"),
                                fieldWithPath("[].codeTarget").description("적용 대상")
                        )
                ));

        verify(couponStateCodeService).getCouponStateCodes();
    }
}