package com.nhnacademy.bookpubshop.product.relationship.controller;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.MockMvc;

/**
 * ProductSaleStateCodeController ?????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WebMvcTest(ProductSaleStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductSaleStateCodeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductSaleStateCodeService productSaleStateCodeService;
    ObjectMapper mapper;
    ProductSaleStateCode productSaleStateCode;
    GetProductSaleStateCodeResponseDto responseDto;
    String url;
    String tokenUrl;

    @BeforeEach
    void setUp() {
        url = "/api/state/productSale";
        tokenUrl = "/token/state/productSale";
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();

        responseDto = new GetProductSaleStateCodeResponseDto(
                1,
                productSaleStateCode.getCodeCategory(),
                productSaleStateCode.isCodeUsed(),
                productSaleStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("?????? ?????????????????? ?????? ??????")
    void productSaleStateCodeList() throws Exception {
        List<GetProductSaleStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(productSaleStateCodeService.getAllProductSaleStateCode())
                .thenReturn(responses);

        mockMvc.perform(get(tokenUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].codeNo").value(1))
                .andExpect(jsonPath("$[0].codeCategory").value(productSaleStateCode.getCodeCategory()))
                .andExpect(jsonPath("$[0].codeInfo").value(productSaleStateCode.getCodeInfo()))
                .andExpect(jsonPath("$[0].codeUsed").value(productSaleStateCode.isCodeUsed()))
                .andDo(print())
                .andDo(document("get-productSaleStateCodes",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("?????????????????? ????????????"),
                                fieldWithPath("[].codeCategory").description("?????????????????? ??????"),
                                fieldWithPath("[].codeUsed").description("?????????????????? ?????? ??????"),
                                fieldWithPath("[].codeInfo").description("????????????????????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ???????????? ?????? ?????????")
    void productSaleStateCodeUsedList() throws Exception {
        // given
        List<GetProductSaleStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        // when
        when(productSaleStateCodeService.getAllProductSaleStateCodeUsed())
                .thenReturn(responses);

        // then
        mockMvc.perform(get(url + "/used")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-usedProductSaleStateCodes",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("?????????????????? ????????????"),
                                fieldWithPath("[].codeCategory").description("?????????????????? ??????"),
                                fieldWithPath("[].codeUsed").description("?????????????????? ?????? ??????"),
                                fieldWithPath("[].codeInfo").description("????????????????????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????????????????? ????????? ?????? ??????")
    void productSaleStateCodeDetails() throws Exception {
        when(productSaleStateCodeService.getSaleCodeById(1))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{codeNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(responseDto.getCodeNo()))
                .andExpect(jsonPath("$.codeCategory").value(productSaleStateCode.getCodeCategory()))
                .andExpect(jsonPath("$.codeInfo").value(productSaleStateCode.getCodeInfo()))
                .andExpect(jsonPath("$.codeUsed").value(productSaleStateCode.isCodeUsed()))
                .andDo(print())
                .andDo(document("get-productSaleStateCode",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("codeNo").description("????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("codeNo").description("?????????????????? ????????????"),
                                fieldWithPath("codeCategory").description("?????????????????? ??????"),
                                fieldWithPath("codeUsed").description("?????????????????? ?????? ??????"),
                                fieldWithPath("codeInfo").description("????????????????????? ?????? ??????")
                        )));

        verify(productSaleStateCodeService, times(1))
                .getSaleCodeById(1);
    }

    @Test
    @DisplayName("?????????????????? ???????????? ?????? ??????")
    void productSaleStateCodeModifyUsed() throws Exception {
        when(productSaleStateCodeService.setUsedSaleCodeById(productSaleStateCode.getCodeNo(), productSaleStateCode.isCodeUsed()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(tokenUrl + "/{codeNo}", 1)
                        .param("used", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("productSaleStateCode-modifyUsed",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("codeNo").description("??????????????? ????????? ?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("used").description("?????? ??????")
                        )));
    }
}