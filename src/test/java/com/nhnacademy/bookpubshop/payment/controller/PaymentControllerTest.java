package com.nhnacademy.bookpubshop.payment.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.payment.service.PaymentService;
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
import org.springframework.test.web.servlet.MockMvc;

/**
 * 결제 컨트롤러 테스트입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@WebMvcTest(PaymentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(ShopAdviceController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PaymentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentService paymentService;

    String url = "/api/payment";

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("payment 생성하는 메소드.")
    void createPayment() throws Exception {
        doNothing().when(paymentService).createPayment(anyString(), anyString(), anyLong());

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("paymentKey", "payment")
                        .param("orderId", "orderId")
                        .param("amount", String.valueOf(1500L)))
                .andExpect(status().isCreated())
                .andDo(document("create-payment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("paymentKey").description("결제 키 고유번호 입니다."),
                                parameterWithName("orderId").description("주문 키 고유번호 입니다."),
                                parameterWithName("amount").description("결제 할 금액입니다.")
                        )));
    }

    @Test
    @DisplayName("주문이 정당한지 검증 --> 검증된 주문이였음을 확인")
    void verify() throws Exception {
        when(paymentService.verifyPayment(anyString(), anyLong())).thenReturn(true);

        mockMvc.perform(post(url + "/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("orderId", "orderId")
                        .param("amount", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("verify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("orderId").description("주문의 고유번호 입니다."),
                                parameterWithName("amount").description("결제 할 금액입니다.")
                        )));
    }

    @Test
    @DisplayName("주문이 정당한지 검증 --> 검증되지 않은 주문이였음을 확인")
    void verify_failed() throws Exception {
        when(paymentService.verifyPayment(anyString(), anyLong())).thenReturn(false);

        mockMvc.perform(post(url + "/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("orderId", "orderId")
                        .param("amount", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(document("verify-false",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("orderId").description("주문의 고유번호 입니다."),
                                parameterWithName("amount").description("결제 할 금액입니다.")
                        )));
    }
}