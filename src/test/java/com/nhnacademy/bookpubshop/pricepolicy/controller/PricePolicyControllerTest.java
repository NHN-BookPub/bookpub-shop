package com.nhnacademy.bookpubshop.pricepolicy.controller;

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
import com.nhnacademy.bookpubshop.pricepolicy.dto.request.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.service.PricePolicyService;
import com.nhnacademy.bookpubshop.state.PricePolicyState;
import java.time.LocalDateTime;
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
 * ?????? ?????? ???????????? ?????????.
 *
 * @author : ?????????
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
    String tokenUrl = "/token/state/pricepolicies";
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        localDateTime = LocalDateTime.of(2023, 1, 30, 22, 46);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void getAllPolicies_Test() throws Exception {
        // given
        GetPricePolicyResponseDto dto = new GetPricePolicyResponseDto(1, "?????????", 3000L, localDateTime);
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
                                fieldWithPath("[].policyNo").description("?????? ??????"),
                                fieldWithPath("[].policyName").description("?????????"),
                                fieldWithPath("[].policyFee").description("??????"),
                                fieldWithPath("[].createdAt").description("????????????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void createPricePolicy_Success_Test() throws Exception {
        // given
        CreatePricePolicyRequestDto dto = new CreatePricePolicyRequestDto();
        ReflectionTestUtils.setField(dto, "policyName", PricePolicyState.PACKAGING.getName());
        ReflectionTestUtils.setField(dto, "policyFee", 3000L);

        // when
        doNothing().when(pricePolicyService)
                .createPricePolicy(dto);

        // then
        mockMvc.perform(post(tokenUrl)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("price-policy-add-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyName").description("?????? ??????"),
                                fieldWithPath("policyFee").description("??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????")
    void getPricePolicy_Test() throws Exception {
        // given
        GetPricePolicyResponseDto dto = new GetPricePolicyResponseDto(1, "?????????", 3000L, localDateTime);

        // when
        when(pricePolicyService.getPricePoliciesByName(dto.getPolicyName()))
                .thenReturn(List.of(dto));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{policyName}", dto.getPolicyName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].policyNo").value(dto.getPolicyNo()))
                .andExpect(jsonPath("$[0].policyName").value(dto.getPolicyName()))
                .andExpect(jsonPath("$[0].policyFee").value(dto.getPolicyFee()))
                .andDo(print())
                .andDo(document("price-policies-get",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("policyName").description("?????? ??????")),
                        responseFields(
                                fieldWithPath("[].policyNo").description("?????? ??????"),
                                fieldWithPath("[].policyName").description("?????????"),
                                fieldWithPath("[].policyFee").description("??????"),
                                fieldWithPath("[].createdAt").description("????????????")
                        )));
    }

    @Test
    @DisplayName("????????? ????????? ?????????, ????????? ?????? ?????? ?????????")
    void getOrderRequiredPricePolicy_Test() throws Exception {
        // given
        GetOrderPolicyResponseDto ship = new GetOrderPolicyResponseDto(1, "?????????", 3000L);
        GetOrderPolicyResponseDto pack = new GetOrderPolicyResponseDto(2, "?????????", 2000L);

        // when
        when(pricePolicyService.getOrderRequestPolicy())
                .thenReturn(List.of(ship, pack));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].policyNo").value(ship.getPolicyNo()))
                .andExpect(jsonPath("$[0].policyName").value(ship.getPolicyName()))
                .andExpect(jsonPath("$[0].policyFee").value(ship.getPolicyFee()))
                .andExpect(jsonPath("$[1].policyNo").value(pack.getPolicyNo()))
                .andExpect(jsonPath("$[1].policyName").value(pack.getPolicyName()))
                .andExpect(jsonPath("$[1].policyFee").value(pack.getPolicyFee()))
                .andDo(print())
                .andDo(document("order-required-price-policies-get",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].policyNo").description("?????? ??????"),
                                fieldWithPath("[].policyName").description("?????????"),
                                fieldWithPath("[].policyFee").description("??????")
                        )));
    }
}