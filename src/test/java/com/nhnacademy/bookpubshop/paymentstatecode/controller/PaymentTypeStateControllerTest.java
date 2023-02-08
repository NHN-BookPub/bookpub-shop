package com.nhnacademy.bookpubshop.paymentstatecode.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.service.PaymentStateService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 결제유형상태 컨트롤러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@WebMvcTest(PaymentStateController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PaymentTypeStateControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PaymentStateService paymentStateService;
    PaymentStateCode paymentStateCode;
    GetPaymentStateResponseDto getPaymentStateResponseDto;

    String url = "/api/payment/state";
    String tokenUrl = "/token/payment/state";

    @BeforeEach
    void setUp() {
        paymentStateCode = PaymentStateCodeDummy.dummy();
        getPaymentStateResponseDto = new GetPaymentStateResponseDto(
                paymentStateCode.getCodeNo(),
                paymentStateCode.getCodeName(),
                paymentStateCode.isCodeUsed(),
                paymentStateCode.getCodeInfo()
        );
    }

    @Test
    @DisplayName("모든 결제 유형을 조회하는 메소드")
    void getPaymentList() throws Exception {
        Mockito.when(paymentStateService.getAllPaymentState()).thenReturn(List.of(getPaymentStateResponseDto));

        mockMvc.perform(get(tokenUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].codeNo").value(getPaymentStateResponseDto.getCodeNo()))
                .andExpect(jsonPath("$.[0].codeName").value(getPaymentStateResponseDto.getCodeName()))
                .andExpect(jsonPath("$.[0].codeUsed").value(getPaymentStateResponseDto.isCodeUsed()))
                .andExpect(jsonPath("$.[0].codeInfo").value(getPaymentStateResponseDto.getCodeInfo()))
                .andDo(document("paymentStateList-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("결제상태 코드번호"),
                                fieldWithPath("[].codeName").description("결제상태 코드명"),
                                fieldWithPath("[].codeUsed").description("결제상태 코드사용여부"),
                                fieldWithPath("[].codeInfo").description("결제상태 코드설명")
                        )));
    }

    @Test
    @DisplayName("타입명으로 결제타입 가져오는 메소드")
    void productSaleStateCodeDetails() throws Exception {
        Mockito.when(paymentStateService.getPaymentState("state"))
                .thenReturn(paymentStateCode);

        mockMvc.perform(get(url + "/{stateName}", "state")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(paymentStateCode.getCodeNo()))
                .andExpect(jsonPath("$.codeName").value(paymentStateCode.getCodeName()))
                .andExpect(jsonPath("$.codeUsed").value(paymentStateCode.isCodeUsed()))
                .andExpect(jsonPath("$.codeInfo").value(paymentStateCode.getCodeInfo()))
                .andDo(document("paymentState-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("codeNo").description("결제유형 코드번호"),
                                fieldWithPath("codeName").description("결제유형 코드명"),
                                fieldWithPath("codeUsed").description("결제유형 코드사용여부"),
                                fieldWithPath("codeInfo").description("결제유형 코드설명")
                        )));
    }
}