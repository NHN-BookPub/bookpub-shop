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
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductPolicyService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * A class for testing ProductPolicyController.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@AutoConfigureRestDocs
@WebMvcTest(ProductPolicyController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductPolicyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductPolicyService productPolicyService;
    ObjectMapper mapper;
    ProductPolicy productPolicy;
    CreateModifyProductPolicyRequestDto requestDto;
    GetProductPolicyResponseDto responseDto;
    String url = "/api/policy/product";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        productPolicy = ProductPolicyDummy.dummy();

        requestDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(requestDto,
                "policyMethod", productPolicy.getPolicyMethod());
        ReflectionTestUtils.setField(requestDto,
                "policySaved", productPolicy.isPolicySaved());
        ReflectionTestUtils.setField(requestDto,
                "saveRate", productPolicy.getSaveRate());

        responseDto = new GetProductPolicyResponseDto(
                productPolicy.getPolicyNo(),
                productPolicy.getPolicyMethod(),
                productPolicy.isPolicySaved(),
                productPolicy.getSaveRate());
    }

    @Test
    @DisplayName("상품정책 등록 성공")
    void createProductPolicy() throws Exception {
        when(productPolicyService.createProductPolicy(requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        BDDMockito.then(productPolicyService)
                .should()
                .createProductPolicy(any(CreateModifyProductPolicyRequestDto.class));
    }

    @Test
    @DisplayName("전체 상품정책 조회 성공")
    void getProductPolicies() throws Exception {
        List<GetProductPolicyResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(productPolicyService.getProductPolicies())
                .thenReturn(responses);

        mockMvc.perform(get(url)
                        .contentType(mapper.writeValueAsString(responses))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].policyNo").value(productPolicy.getPolicyNo()))
                .andExpect(jsonPath("$[0].policyMethod").value(productPolicy.getPolicyMethod()))
                .andExpect(jsonPath("$[0].saveRate").value(productPolicy.getSaveRate()))
                .andExpect(jsonPath("$[0].policySaved").value(productPolicy.isPolicySaved()))
                .andDo(print());
    }

    @Test
    @DisplayName("단일 상품정책 조회 성공")
    void getProductPolicy() throws Exception {
        when(productPolicyService.getProductPolicyById(anyInt()))
                .thenReturn(responseDto);

        mockMvc.perform(get(url + "/{policyNo}", anyInt())
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNo").value(productPolicy.getPolicyNo()))
                .andExpect(jsonPath("$.policyMethod").value(productPolicy.getPolicyMethod()))
                .andExpect(jsonPath("$.saveRate").value(productPolicy.getSaveRate()))
                .andExpect(jsonPath("$.policySaved").value(productPolicy.isPolicySaved()))
                .andDo(print());

        verify(productPolicyService, times(1))
                .getProductPolicyById(anyInt());
    }

    @Test
    @DisplayName("상품정책 수정 성공")
    void modifyProductPolicy() throws Exception {
        when(productPolicyService.modifyProductPolicyById(productPolicy.getPolicyNo(), requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(put(url + "/{policyNo}", 1)
                        .content(mapper.writeValueAsString(responseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        BDDMockito.then(productPolicyService)
                .should()
                .modifyProductPolicyById(anyInt(), any(CreateModifyProductPolicyRequestDto.class));
    }
}