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
    ObjectMapper objectMapper;

    String url = "/api/payment";

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
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

    @Test
    @DisplayName("주문환불 validation orderNo 없음")
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
                                fieldWithPath("[].message").description("주문번호는 null 이 될수없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문환불 validation cancelReason 없음")
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
                                fieldWithPath("[].message").description("cancelReason 은 null 이 될수없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문환불 validation cancelReason 없음")
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
                                fieldWithPath("[].message").description("사이즈는 1부터 200 안에 되어야합니다.")
                        )));
    }

    @Test
    @DisplayName("주문환불 validation cancelReason 없음")
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
                                fieldWithPath("orderNo").description("주문번호가 기입됩니다."),
                                fieldWithPath("cancelReason").description("취소사유가 기입됩니다.")
                        )));

    }

    @Test
    @DisplayName("주문상품 환불 validation orderNo 실패")
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
                                fieldWithPath("[].message").description("주문번호는 null 될수 없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불 validation orderNo 실패")
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
                                fieldWithPath("[].message").description("주문상품번호는 null 이 될 수 없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불 validation cancelReason 실패")
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
                                fieldWithPath("[].message").description("취소사유는 null 이 될 수 없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불 validation cancelReason 실패")
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
                                fieldWithPath("[].message").description("취소이유는 1~200 자 내외여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불성공")
    void refundOrderProductSuccess() throws Exception {
        doNothing().when(paymentService).refundOrderProduct(any());
        OrderProductRefundRequestDto dto = new OrderProductRefundRequestDto();
        ReflectionTestUtils.setField(dto, "orderNo", 1L);
        ReflectionTestUtils.setField(dto, "orderProductNo", 1L);
        ReflectionTestUtils.setField(dto, "cancelReason", "취소사유");

        mockMvc.perform(post("/token/payment/order-product/members/{memberNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("payment-refundProduct",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderNo").description("주문번호가 기입"),
                                fieldWithPath("orderProductNo").description("주문상품번호 기입"),
                                fieldWithPath("cancelReason").description("취소사유가")
                        )));
    }

    @Test
    @DisplayName("주문 상품 교환 신청 PUT")
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
    @DisplayName("주문상품 환불 validation orderNumber null 실패")
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
                                fieldWithPath("[].message").description("주문번호는 null 될수 없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불 validation orderProductNo null 실패")
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
                                fieldWithPath("[].message").description("주문상품번호는 null 이 될 수 없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불 validation cancelReason null 실패")
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
                                fieldWithPath("[].message").description("취소사유는 null 이 될 수 없습니다.")
                        )));
    }

    @Test
    @DisplayName("주문상품 환불 validation cancelReason null 실패")
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
                                fieldWithPath("[].message").description("취소이유는 1~200 자 내외여야 합니다.")
                        )));
    }

}