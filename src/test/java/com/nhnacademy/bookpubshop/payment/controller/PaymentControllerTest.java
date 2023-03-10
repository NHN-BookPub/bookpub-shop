package com.nhnacademy.bookpubshop.payment.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.payment.dto.request.OrderProductRefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ?????? ???????????? ??????????????????.
 *
 * @author : ?????????
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
    ObjectMapper objectMapper;

    String url = "/api/payment";

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("payment ???????????? ?????????.")
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
                                parameterWithName("paymentKey").description("?????? ??? ???????????? ?????????."),
                                parameterWithName("orderId").description("?????? ??? ???????????? ?????????."),
                                parameterWithName("amount").description("?????? ??? ???????????????.")
                        )));
    }

    @Test
    @DisplayName("????????? ???????????? ?????? --> ????????? ?????????????????? ??????")
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
                                parameterWithName("orderId").description("????????? ???????????? ?????????."),
                                parameterWithName("amount").description("?????? ??? ???????????????.")
                        )));
    }

    @Test
    @DisplayName("????????? ???????????? ?????? --> ???????????? ?????? ?????????????????? ??????")
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
                                parameterWithName("orderId").description("????????? ???????????? ?????????."),
                                parameterWithName("amount").description("?????? ??? ???????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? validation orderNo ??????")
    void refundOrderFailTest() throws Exception {
        doNothing().when(paymentService).refundOrder(any());
        RefundRequestDto dto = new RefundRequestDto(null, "resson");
        mockMvc.perform(post("/token/payment/order/{memberNo}}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refund-Fail-orderNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? null ??? ??????????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? validation cancelReason ??????")
    void refundOrderFailTest2() throws Exception {
        doNothing().when(paymentService).refundOrder(any());
        RefundRequestDto dto = new RefundRequestDto(1L, null);
        mockMvc.perform(post("/token/payment/order/{memberNo}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refund-Fail-cancel-reason",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("cancelReason ??? null ??? ??????????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? validation cancelReason ??????")
    void refundOrderFailTest3() throws Exception {
        doNothing().when(paymentService).refundOrder(any());
        RefundRequestDto dto = new RefundRequestDto(1L, "");
        mockMvc.perform(post("/token/payment/order/{memberNo}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refund-Fail-cancel-reason-size",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? 1?????? 200 ?????? ??????????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? validation cancelReason ??????")
    void refundOrderSuccess() throws Exception {
        doNothing().when(paymentService).refundOrder(any());
        RefundRequestDto dto = new RefundRequestDto(1L, "sadfsafdf");
        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/payment/order/{memberNo}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("payment-refund-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderNo").description("??????????????? ???????????????."),
                                fieldWithPath("cancelReason").description("??????????????? ???????????????.")
                        )));

    }

    @Test
    @DisplayName("???????????? ?????? validation orderNo ??????")
    void refundOrderProductFailTest() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto = new OrderProductRefundRequestDto();
        ReflectionTestUtils.setField(dto, "orderNo", null);
        ReflectionTestUtils.setField(dto, "orderProductNo", 1L);
        ReflectionTestUtils.setField(dto, "cancelReason", "cancelReason");

        mockMvc.perform(post("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refundProduct-fail-orderNo",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? null ?????? ????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? validation orderNo ??????")
    void refundOrderProductFailOrderNoFail2() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto = new OrderProductRefundRequestDto();
        ReflectionTestUtils.setField(dto, "orderNo", 1L);
        ReflectionTestUtils.setField(dto, "orderProductNo", null);
        ReflectionTestUtils.setField(dto, "cancelReason", "cancelReason");

        mockMvc.perform(post("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refundProduct-fail-orderProductNo",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????????????????? null ??? ??? ??? ????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? validation cancelReason ??????")
    void refundOrderProductFailOrderNoFail3() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto = new OrderProductRefundRequestDto();
        ReflectionTestUtils.setField(dto, "orderNo", 1L);
        ReflectionTestUtils.setField(dto, "orderProductNo", 1L);
        ReflectionTestUtils.setField(dto, "cancelReason", null);

        mockMvc.perform(post("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refundProduct-fail-cancelReason-null",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? null ??? ??? ??? ????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? validation cancelReason ??????")
    void refundOrderProductFailOrderNoFail4() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto = new OrderProductRefundRequestDto();
        ReflectionTestUtils.setField(dto, "orderNo", 1L);
        ReflectionTestUtils.setField(dto, "orderProductNo", 1L);
        ReflectionTestUtils.setField(dto, "cancelReason", "");

        mockMvc.perform(post("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-refundProduct-fail-cancelReason-size",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? 1~200 ??? ???????????? ?????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ????????????")
    void refundOrderProductSuccess() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto = new OrderProductRefundRequestDto();
        ReflectionTestUtils.setField(dto, "orderNo", 1L);
        ReflectionTestUtils.setField(dto, "orderProductNo", 1L);
        ReflectionTestUtils.setField(dto, "cancelReason", "????????????");

        mockMvc.perform(post("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("payment-refundProduct",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderNo").description("??????????????? ??????"),
                                fieldWithPath("orderProductNo").description("?????????????????? ??????"),
                                fieldWithPath("cancelReason").description("???????????????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? PUT")
    void exChangeOrderProductTest() throws Exception {
        OrderProductRefundRequestDto dto =
                new OrderProductRefundRequestDto(1L, 1L, "cancelReason");
        doNothing().when(paymentService).exchangeOrderProduct(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("payment-put-refund",
                        preprocessRequest(),
                        preprocessResponse()));
    }

    @Test
    @DisplayName("???????????? ?????? validation orderNumber null ??????")
    void exChangeOrderProductFail1() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto =
                new OrderProductRefundRequestDto(null, 1L, "cancelReason");
        doNothing().when(paymentService).exchangeOrderProduct(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-put-refund-fail-orderNo-null",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? null ?????? ????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? validation orderProductNo null ??????")
    void exChangeOrderProductFail2() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto =
                new OrderProductRefundRequestDto(1L, null, "cancelReason");
        doNothing().when(paymentService).exchangeOrderProduct(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-put-refund-fail-orderProduct-null",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????????????????? null ??? ??? ??? ????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? validation cancelReason null ??????")
    void exChangeOrderProductFail3() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto =
                new OrderProductRefundRequestDto(1L, 1L, null);
        doNothing().when(paymentService).exchangeOrderProduct(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-put-refund-fail-cancelReason-null",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? null ??? ??? ??? ????????????.")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? validation cancelReason null ??????")
    void exChangeOrderProductFail4() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto =
                new OrderProductRefundRequestDto(1L, 1L, "");
        doNothing().when(paymentService).exchangeOrderProduct(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("payment-put-refund-fail-cancelReason-long",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? 1~200 ??? ???????????? ?????????.")
                        )));
    }

}