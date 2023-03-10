package com.nhnacademy.bookpubshop.sales.controllerr;

import static com.nhnacademy.bookpubshop.sales.dummy.TotalSaleDummy.orderCntDummy;
import static com.nhnacademy.bookpubshop.sales.dummy.TotalSaleDummy.totalSaleDummy;
import static com.nhnacademy.bookpubshop.sales.dummy.TotalSaleDummy.totalSaleYearDummy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import com.nhnacademy.bookpubshop.sales.service.SalesService;
import com.nhnacademy.bookpubshop.tier.controller.TierController;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Some description here.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(SalesController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class SalesControllerTest {

    @Autowired
    MockMvc mvc;
    ObjectMapper objectMapper;
    @MockBean
    SalesService service;
    TotalSaleDto totalSaleDto;
    TotalSaleYearDto totalSaleYearDto;
    OrderCntResponseDto orderCntResponseDto;

    @BeforeEach
    void setUp() {
        totalSaleDto = totalSaleDummy();
        totalSaleYearDto = totalSaleYearDummy();
        orderCntResponseDto = orderCntDummy();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @DisplayName("??????????????? ??????")
    @Test
    void salesList() throws Exception {
        when(service.getSales(null, null))
                .thenReturn(List.of(totalSaleDto));

        mvc.perform(get("/token/sales")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("sales-month",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].cancelPaymentCnt").description("??????????????? ????????? ???????????????."),
                                fieldWithPath("[].cancelPaymentAmount").description("????????? ????????? ???????????????."),
                                fieldWithPath("[].cancelOrderCnt").description("??????????????? ????????? ???????????????."),
                                fieldWithPath("[].saleCnt").description("??????????????? ???????????????."),
                                fieldWithPath("[].saleAmount").description("??????????????? ???????????????."),
                                fieldWithPath("[].total").description("??? ????????????????????????.")
                        )));
        verify(service, times(1)).getSales(null, null);

    }

    @DisplayName("????????? ???????????? ??????")
    @Test
    void orderCnt() throws Exception {
        when(service.getOrderCnt())
                .thenReturn(List.of(orderCntResponseDto));

        mvc.perform(get("/token/order-count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("sales-order-cnt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].date").description("??????????????? ????????? ???????????????."),
                                fieldWithPath("[].orderCnt").description("????????? ????????? ???????????????.")
                        )));

        verify(service, times(1)).getOrderCnt();
    }

    @DisplayName("?????? ???????????? ??????")
    @Test
    void salesMonthList() throws Exception {
        when(service.getTotalSaleCurrentYear(null, null))
                .thenReturn(List.of(totalSaleYearDto));

        mvc.perform(get("/token/sales-month")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("sales-year",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].cancelPaymentCnt").description("??????????????? ????????? ???????????????."),
                                fieldWithPath("[].cancelPaymentAmount").description("????????? ????????? ???????????????."),
                                fieldWithPath("[].cancelOrderCnt").description("??????????????? ????????? ???????????????."),
                                fieldWithPath("[].saleCnt").description("??????????????? ???????????????."),
                                fieldWithPath("[].saleAmount").description("??????????????? ???????????????."),
                                fieldWithPath("[].total").description("??? ????????????????????????."),
                                fieldWithPath("[].month").description("?????? ?????? ???????????????.")
                        )));

        verify(service, times(1)).getTotalSaleCurrentYear(null, null);
    }
}