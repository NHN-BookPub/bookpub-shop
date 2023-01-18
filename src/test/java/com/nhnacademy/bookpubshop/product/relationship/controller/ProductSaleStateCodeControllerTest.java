package com.nhnacademy.bookpubshop.product.relationship.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductSaleStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
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
@WebMvcTest(ProductSaleStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductSaleStateCodeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductSaleStateCodeService productSaleStateCodeService;
    ObjectMapper mapper;
    ProductSaleStateCode productSaleStateCode;
    CreateProductSaleStateCodeRequestDto requestDto;
    GetProductSaleStateCodeResponseDto responseDto;
    String url;

    @BeforeEach
    void setUp() {
        url = "/api/state/productSale";
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        requestDto = new CreateProductSaleStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto,
                "codeCategory",
                productSaleStateCode.getCodeCategory());
        ReflectionTestUtils.setField(requestDto,
                "codeUsed",
                productSaleStateCode.isCodeUsed());
        ReflectionTestUtils.setField(requestDto,
                "codeInfo",
                productSaleStateCode.getCodeInfo());

        responseDto = new GetProductSaleStateCodeResponseDto(
                1,
                productSaleStateCode.getCodeCategory(),
                productSaleStateCode.isCodeUsed(),
                productSaleStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("모든 판매상태코드 조회 성공")
    void getAllSaleStateCode() throws Exception {
        List<GetProductSaleStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(productSaleStateCodeService.getAllProductSaleStateCode())
                .thenReturn(responses);

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(responses)))
                .andExpect(jsonPath("$[0].codeNo").value(1))
                .andExpect(jsonPath("$[0].codeCategory").value(productSaleStateCode.getCodeCategory()))
                .andExpect(jsonPath("$[0].codeInfo").value(productSaleStateCode.getCodeInfo()))
                .andExpect(jsonPath("$[0].codeUsed").value(productSaleStateCode.isCodeUsed()))
                .andDo(print());
    }

    @Test
    @DisplayName("사용 중인 모든 판매 상태코드 조회 테스트")
    void getALlSaleStatCodeUsed() throws Exception {
        // given
        List<GetProductSaleStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        // when
        when(productSaleStateCodeService.getAllProductSaleStateCodeUsed())
                .thenReturn(responses);

        // then
        mockMvc.perform(get(url + "/used")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("판매상태코드 생성 성공")
    void createProductSaleStateCode() throws Exception {
        when(productSaleStateCodeService.createSaleCode(requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        BDDMockito.then(productSaleStateCodeService)
                .should()
                .createSaleCode(any(CreateProductSaleStateCodeRequestDto.class));
    }

    @Test
    @DisplayName("판매상태코드 번호로 조회 성공")
    void getProductSaleStateCodeById() throws Exception {
        when(productSaleStateCodeService.getSaleCodeById(1))
                .thenReturn(responseDto);

        mockMvc.perform(get(url + "/{codeNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(responseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(responseDto.getCodeNo()))
                .andExpect(jsonPath("$.codeCategory").value(productSaleStateCode.getCodeCategory()))
                .andExpect(jsonPath("$.codeInfo").value(productSaleStateCode.getCodeInfo()))
                .andExpect(jsonPath("$.codeUsed").value(productSaleStateCode.isCodeUsed()))
                .andDo(print());

        verify(productSaleStateCodeService, times(1))
                .getSaleCodeById(1);
    }

    @Test
    @DisplayName("판매상태코드 사용여부 설정 성공")
    void setUsedSaleCodeById() throws Exception {
        when(productSaleStateCodeService.setUsedSaleCodeById(productSaleStateCode.getCodeNo(), productSaleStateCode.isCodeUsed()))
                .thenReturn(responseDto);

        mockMvc.perform(put(url + "/{codeNo}", 1)
                        .param("used", "true")
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}