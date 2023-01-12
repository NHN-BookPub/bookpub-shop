package com.nhnacademy.bookpubshop.product.relationship.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductTypeStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ProductTypeStateCodeController 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(ProductTypeStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductTypeStateCodeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductTypeStateCodeService productTypeStateCodeService;
    @Autowired
    ObjectMapper mapper;
    ProductTypeStateCode productTypeStateCode;
    CreateProductTypeStateCodeRequestDto requestDto;
    GetProductTypeStateCodeResponseDto responseDto;
    String url;

    @BeforeEach
    void setUp() {
        url = "/api/state/productType";
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        requestDto = new CreateProductTypeStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto,
                "codeName",
                productTypeStateCode.getCodeName());
        ReflectionTestUtils.setField(requestDto,
                "codeUsed",
                productTypeStateCode.isCodeUsed());
        ReflectionTestUtils.setField(requestDto,
                "codeInfo",
                productTypeStateCode.getCodeInfo());

        responseDto = new GetProductTypeStateCodeResponseDto(
                1,
                productTypeStateCode.getCodeName(),
                productTypeStateCode.isCodeUsed(),
                productTypeStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("모든 유형 조회 성공")
    void getAllTypeCodes() throws Exception {
        List<GetProductTypeStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(productTypeStateCodeService.getAllTypeStateCodes())
                .thenReturn(responses);

        mockMvc.perform(get(url)
                        .content(mapper.writeValueAsString(responses))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codeNo")
                        .value(1))
                .andExpect(jsonPath("$[0].codeName")
                        .value(productTypeStateCode.getCodeName()))
                .andExpect(jsonPath("$[0].codeInfo")
                        .value(productTypeStateCode.getCodeInfo()))
                .andExpect(jsonPath("$[0].codeUsed")
                        .value(productTypeStateCode.isCodeUsed()))
                .andDo(print());
    }

    @Test
    @DisplayName("유형 코드 생성 성공")
    void createTypeCode() throws Exception {
        when(productTypeStateCodeService.createTypeStateCode(requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        BDDMockito.then(productTypeStateCodeService)
                .should()
                .createTypeStateCode(any(CreateProductTypeStateCodeRequestDto.class));
    }

    @Test
    @DisplayName("유형 코드 번호로 조회 성공")
    void getTypeCodeById() throws Exception {
        when(productTypeStateCodeService.getTypeStateCodeById(anyInt()))
                .thenReturn(responseDto);

        mockMvc.perform(get(url + "/{codeNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(responseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(mapper.writeValueAsString(responseDto.getCodeNo())))
                .andExpect(jsonPath("$.codeName").value(productTypeStateCode.getCodeName()))
                .andExpect(jsonPath("$.codeInfo").value(productTypeStateCode.getCodeInfo()))
                .andExpect(jsonPath("$.codeUsed").value(productTypeStateCode.isCodeUsed()))
                .andDo(print());
    }

    @Test
    @DisplayName("유형 코드 번호로 사용여부 설정 성공")
    void setUsedTypeCodeById() throws Exception {
        when(productTypeStateCodeService.setUsedTypeCodeById(productTypeStateCode.getCodeNo(), productTypeStateCode.isCodeUsed()))
                .thenReturn(responseDto);

        mockMvc.perform(delete(url + "/1?used=true")
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}