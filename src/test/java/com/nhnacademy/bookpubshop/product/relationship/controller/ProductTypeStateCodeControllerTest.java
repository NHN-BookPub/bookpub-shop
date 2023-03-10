package com.nhnacademy.bookpubshop.product.relationship.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
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
 * ProductTypeStateCodeController ?????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WebMvcTest(ProductTypeStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductTypeStateCodeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductTypeStateCodeService productTypeStateCodeService;
    @Autowired
    ObjectMapper mapper;
    ProductTypeStateCode productTypeStateCode;
    GetProductTypeStateCodeResponseDto responseDto;
    String url;
    String tokenUrl;

    @BeforeEach
    void setUp() {
        url = "/api/state/productType";
        tokenUrl = "/token/state/productType";

        productTypeStateCode = ProductTypeStateCodeDummy.dummy();

        responseDto = new GetProductTypeStateCodeResponseDto(
                1,
                productTypeStateCode.getCodeName(),
                productTypeStateCode.isCodeUsed(),
                productTypeStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void typeCodeList() throws Exception {
        List<GetProductTypeStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(productTypeStateCodeService.getAllTypeStateCodes())
                .thenReturn(responses);

        mockMvc.perform(get(tokenUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codeNo")
                        .value(1))
                .andExpect(jsonPath("$[0].codeName")
                        .value(productTypeStateCode.getCodeName()))
                .andExpect(jsonPath("$[0].codeInfo")
                        .value(productTypeStateCode.getCodeInfo()))
                .andExpect(jsonPath("$[0].codeUsed")
                        .value(productTypeStateCode.isCodeUsed()))
                .andDo(print())
                .andDo(document("get-productTypeStateCodes",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("???????????? ????????????"),
                                fieldWithPath("[].codeName").description("???????????? ??????"),
                                fieldWithPath("[].codeUsed").description("???????????? ?????? ??????"),
                                fieldWithPath("[].codeInfo").description("??????????????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????? ?????? ?????????")
    void typeCodesUsedList() throws Exception {
        // given
        List<GetProductTypeStateCodeResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        // when
        when(productTypeStateCodeService.getAllTypeStateCodesUsed())
                .thenReturn(responses);

        // then
        mockMvc.perform(get(url + "/used")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codeNo").value(responses.get(0).getCodeNo()))
                .andDo(print())
                .andDo(document("get-usedProductTypeStateCodes",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("???????????? ????????????"),
                                fieldWithPath("[].codeName").description("???????????? ??????"),
                                fieldWithPath("[].codeUsed").description("???????????? ?????? ??????"),
                                fieldWithPath("[].codeInfo").description("??????????????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ??????")
    void typeCodeDetails() throws Exception {
        when(productTypeStateCodeService.getTypeStateCodeById(anyInt()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{codeNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(mapper.writeValueAsString(responseDto.getCodeNo())))
                .andExpect(jsonPath("$.codeName").value(productTypeStateCode.getCodeName()))
                .andExpect(jsonPath("$.codeInfo").value(productTypeStateCode.getCodeInfo()))
                .andExpect(jsonPath("$.codeUsed").value(productTypeStateCode.isCodeUsed()))
                .andDo(print())
                .andDo(document("get-productTypeStateCode",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("codeNo").description("????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("codeNo").description("???????????? ????????????"),
                                fieldWithPath("codeName").description("???????????? ??????"),
                                fieldWithPath("codeUsed").description("???????????? ?????? ??????"),
                                fieldWithPath("codeInfo").description("??????????????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ???????????? ?????? ??????")
    void typeCodeModifyUsed() throws Exception {
        when(productTypeStateCodeService.setUsedTypeCodeById(productTypeStateCode.getCodeNo(), productTypeStateCode.isCodeUsed()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(tokenUrl + "/{codeNo}", 1)
                        .param("used", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("ProductTypeStateCode-modifyUsed",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("codeNo").description("??????????????? ????????? ?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("used").description("?????? ??????")
                        )));
    }
}