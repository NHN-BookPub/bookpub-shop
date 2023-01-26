package com.nhnacademy.bookpubshop.pricepolicy.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.pricepolicy.dto.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.service.PricePolicyService;
import com.nhnacademy.bookpubshop.state.PricePolicyState;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 가격 정책 컨트롤러 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@WebMvcTest(PricePolicyController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PricePolicyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PricePolicyService pricePolicyService;

    ObjectMapper objectMapper;
    String url = "/api/state/pricepolicies";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("모든 가격 정책 조회 테스트")
    void getAllPolicies_Test() throws Exception {
        // given
        GetPricePolicyResponseDto dto = new GetPricePolicyResponseDto(1, "배송비", 3000L);
        List<GetPricePolicyResponseDto> list = List.of(dto);

        // when
        when(pricePolicyService.getPricePolicies())
                .thenReturn(list);

        // then
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].policyNo").value(dto.getPolicyNo()))
                .andExpect(jsonPath("$[0].policyName").value(dto.getPolicyName()))
                .andExpect(jsonPath("$[0].policyFee").value(dto.getPolicyFee()))
                .andDo(print())
                .andDo(document("price-policies-get",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].policyNo").description("정책 번호"),
                                fieldWithPath("[].policyName").description("정책명"),
                                fieldWithPath("[].policyFee").description("가격")
                        )));
    }

    @Test
    @DisplayName("가격 정책 등록 성공 테스트")
    void createPricePolicy_Success_Test() throws Exception {
        // given
        CreatePricePolicyRequestDto dto = new CreatePricePolicyRequestDto();
        ReflectionTestUtils.setField(dto, "policyName", PricePolicyState.PACKAGING.getName());
        ReflectionTestUtils.setField(dto, "policyFee", 3000L);

        // when
        doNothing().when(pricePolicyService)
                .createPricePolicy(dto);

        // then
        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("price-policy-add-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyName").description("정책 이름"),
                                fieldWithPath("policyFee").description("가격")
                        )));
    }

    @Test
    @DisplayName("가격 정책 수정 성공 테스트")
    void modifyPricePolicy_Success_Test() throws Exception {
        // given
        Long fee = 1000L;

        // when
        doNothing().when(pricePolicyService)
                .modifyPricePolicyFee(1, fee);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/{policyNo}", 1)
                        .param("fee", String.valueOf(fee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("price-policy-modify-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(parameterWithName("policyNo").description("정책 번호")),
                        requestParameters(
                                parameterWithName("fee").description("가격")
                        )));
    }

    @Test
    @DisplayName("정책 단건 조회 테스트")
    void getPricePolicy_Test() throws Exception {
        // given
        GetPricePolicyResponseDto dto = new GetPricePolicyResponseDto(1, "배송비", 3000L);

        // when
        when(pricePolicyService.getPricePolicyById(anyInt()))
                .thenReturn(dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{policyNo}", dto.getPolicyNo())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.policyNo").value(dto.getPolicyNo()))
                .andExpect(jsonPath("$.policyName").value(dto.getPolicyName()))
                .andExpect(jsonPath("$.policyFee").value(dto.getPolicyFee()))
                .andDo(print())
                .andDo(document("price-policy-get",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("policyNo").description("정책 번호")),
                        responseFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyName").description("정책명"),
                                fieldWithPath("policyFee").description("가격")
                        )));
    }
}