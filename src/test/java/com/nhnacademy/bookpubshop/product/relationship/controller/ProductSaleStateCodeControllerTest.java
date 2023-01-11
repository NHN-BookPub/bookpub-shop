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
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductSaleStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
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
        productSaleStateCode = new ProductSaleStateCode(1, "test", true, "test");
        requestDto = new CreateProductSaleStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto, "codeCategory", "test");
        ReflectionTestUtils.setField(requestDto, "codeUsed", true);
        ReflectionTestUtils.setField(requestDto, "codeInfo", "test");

        responseDto = new GetProductSaleStateCodeResponseDto(
                productSaleStateCode.getCodeNumber(),
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
                .andExpect(jsonPath("$[0].codeNumber").value(productSaleStateCode.getCodeNumber()))
                .andExpect(jsonPath("$[0].codeCategory").value(productSaleStateCode.getCodeCategory()))
                .andExpect(jsonPath("$[0].codeInfo").value(productSaleStateCode.getCodeInfo()))
                .andExpect(jsonPath("$[0].codeUsed").value(productSaleStateCode.isCodeUsed()))
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
        when(productSaleStateCodeService.getSaleCodeById(productSaleStateCode.getCodeNumber()))
                .thenReturn(responseDto);

        mockMvc.perform(get(url + "/" + productSaleStateCode.getCodeNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(responseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNumber").value(productSaleStateCode.getCodeNumber()))
                .andExpect(jsonPath("$.codeCategory").value(productSaleStateCode.getCodeCategory()))
                .andExpect(jsonPath("$.codeInfo").value(productSaleStateCode.getCodeInfo()))
                .andExpect(jsonPath("$.codeUsed").value(productSaleStateCode.isCodeUsed()))
                .andDo(print());

        verify(productSaleStateCodeService, times(1))
                .getSaleCodeById(productSaleStateCode.getCodeNumber());
    }

    @Test
    @DisplayName("판매상태코드 사용여부 설정 성공")
    void setUsedSaleCodeById() throws Exception {
        when(productSaleStateCodeService.setUsedSaleCodeById(productSaleStateCode.getCodeNumber(), productSaleStateCode.isCodeUsed()))
                .thenReturn(responseDto);

        mockMvc.perform(delete(url + "/" + productSaleStateCode.getCodeNumber() + "?used=true")
                .content(mapper.writeValueAsString(responseDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}