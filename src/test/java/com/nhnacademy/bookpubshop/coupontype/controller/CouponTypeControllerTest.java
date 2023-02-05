package com.nhnacademy.bookpubshop.coupontype.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import com.nhnacademy.bookpubshop.coupontype.service.CouponTypeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
 * CouponTypeController 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponTypeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CouponTypeControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @MockBean
    CouponTypeService couponTypeService;
    String authPath = "/token/coupon-types";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("쿠폰유형 단건 조회 테스트")
    void CouponTypeDetailTest() throws Exception {
        //given
        GetCouponTypeResponseDto dto = new GetCouponTypeResponseDto(1L, "test_target");
        given(couponTypeService.getCouponType(anyLong()))
                .willReturn(dto);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(authPath + "/{typeNo}", anyLong()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.typeNo", is(dto.getTypeNo()), Long.class))
                .andExpect(jsonPath("$.typeName", equalTo(dto.getTypeName())))
                .andDo(document("coupon-type-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("typeNo").description("Path 로 타입번호 기입")),
                        responseFields(
                                fieldWithPath("typeNo").description("쿠폰 유형 번호"),
                                fieldWithPath("typeName").description("쿠폰 유형 명칭")
                                )
                ));
        verify(couponTypeService).getCouponType(anyLong());
    }

    @Test
    @DisplayName("쿠폰유형 리스트 조회 테스트")
    void CouponTypeListTest() throws Exception {
        //given
        GetCouponTypeResponseDto dto = new GetCouponTypeResponseDto(1L, "test_target");
        given(couponTypeService.getCouponTypes()).willReturn(List.of(dto));

        //when && then
        mockMvc.perform(get(authPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].typeNo", is(dto.getTypeNo()), Long.class))
                .andExpect(jsonPath("$[0].typeName", equalTo(dto.getTypeName())))
                .andDo(document("coupon-type-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].typeNo").description("쿠폰 유형 번호"),
                                fieldWithPath("[].typeName").description("쿠폰 유형 명칭")
                        )
                ));
        verify(couponTypeService).getCouponTypes();
    }
}