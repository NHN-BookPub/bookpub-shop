package com.nhnacademy.bookpubshop.paymenttypestatecode.controller;

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
import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.service.PaymentTypeService;
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
@WebMvcTest(PaymentTypeStateController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PaymentTypeStateControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PaymentTypeService paymentTypeService;

    PaymentTypeStateCode paymentTypeStateCode;
    GetPaymentTypeResponseDto getPaymentTypeResponseDto;

    String url = "/api/payment/type";
    String tokenUrl = "/token/payment/type";

    @BeforeEach
    void setUp() {
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
        getPaymentTypeResponseDto = new GetPaymentTypeResponseDto(
                paymentTypeStateCode.getCodeNo(),
                paymentTypeStateCode.getCodeName(),
                paymentTypeStateCode.isCodeUsed(),
                paymentTypeStateCode.getCodeInfo()
        );
    }

    @Test
    @DisplayName("모든 결제 유형을 조회하는 메소드")
    void getPaymentList() throws Exception {
        Mockito.when(paymentTypeService.getAllPaymentType()).thenReturn(List.of(getPaymentTypeResponseDto));

        mockMvc.perform(get(tokenUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].codeNo").value(getPaymentTypeResponseDto.getCodeNo()))
                .andExpect(jsonPath("$.[0].codeName").value(getPaymentTypeResponseDto.getCodeName()))
                .andExpect(jsonPath("$.[0].codeUsed").value(getPaymentTypeResponseDto.isCodeUsed()))
                .andExpect(jsonPath("$.[0].codeInfo").value(getPaymentTypeResponseDto.getCodeInfo()))
                .andDo(document("paymentTypeList-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("결제유형 코드번호"),
                                fieldWithPath("[].codeName").description("결제유형 코드명"),
                                fieldWithPath("[].codeUsed").description("결제유형 코드사용여부"),
                                fieldWithPath("[].codeInfo").description("결제유형 코드설명")
                        )));
    }

    @Test
    @DisplayName("타입명으로 결제타입 가져오는 메소드")
    void productSaleStateCodeDetails() throws Exception {
        Mockito.when(paymentTypeService.getPaymentType("type"))
                .thenReturn(paymentTypeStateCode);

        mockMvc.perform(get(url + "/{type}", "type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(paymentTypeStateCode.getCodeNo()))
                .andExpect(jsonPath("$.codeName").value(paymentTypeStateCode.getCodeName()))
                .andExpect(jsonPath("$.codeUsed").value(paymentTypeStateCode.isCodeUsed()))
                .andExpect(jsonPath("$.codeInfo").value(paymentTypeStateCode.getCodeInfo()))
                .andDo(document("paymentType-get",
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