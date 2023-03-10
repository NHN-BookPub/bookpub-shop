package com.nhnacademy.bookpubshop.product.relationship.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductPolicyService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * A class for testing ProductPolicyController.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(ProductPolicyController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class ProductPolicyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductPolicyService productPolicyService;
    ObjectMapper mapper;
    ProductPolicy productPolicy;
    CreateModifyProductPolicyRequestDto requestDto;
    GetProductPolicyResponseDto responseDto;
    String url = "/api/policy/product";
    String tokenUrl = "/token/policy/product";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        productPolicy = ProductPolicyDummy.dummy();

        requestDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(requestDto, "policyMethod", productPolicy.getPolicyMethod());
        ReflectionTestUtils.setField(requestDto, "policySaved", productPolicy.isPolicySaved());
        ReflectionTestUtils.setField(requestDto, "saveRate", productPolicy.getSaveRate());

        responseDto = new GetProductPolicyResponseDto(
                1,
                productPolicy.getPolicyMethod(),
                productPolicy.isPolicySaved(),
                productPolicy.getSaveRate());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void createProductPolicy() throws Exception {
        doNothing().when(productPolicyService)
                .createProductPolicy(requestDto);

        mockMvc.perform(post(tokenUrl)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("product-policy-add-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyMethod").description("????????? ????????? ??????(???????????? or ?????????"),
                                fieldWithPath("policySaved").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("saveRate").description("????????? ?????????(%) ??????")
                        )));

        then(productPolicyService)
                .should()
                .createProductPolicy(any(CreateModifyProductPolicyRequestDto.class));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? (Validation)")
    void createProductPolicy_Fail_Test() throws Exception {
        // given
        CreateModifyProductPolicyRequestDto dto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(dto, "policyMethod", "10??????????????????????????????");
        ReflectionTestUtils.setField(dto, "policySaved", true);
        ReflectionTestUtils.setField(dto, "saveRate", 10);

        // when
        doNothing().when(productPolicyService)
                .createProductPolicy(dto);

        // then
        mockMvc.perform(post(tokenUrl)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("10????????? ?????? ??? ????????????."))
                .andDo(print())
                .andDo(document("product-policy-policyMethod-validationFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyMethod").description("????????? ????????? ??????(???????????? or ?????????"),
                                fieldWithPath("policySaved").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("saveRate").description("????????? ?????????(%) ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? 10????????? ?????? ??? ????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ??????")
    void getProductPolicies() throws Exception {
        List<GetProductPolicyResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(productPolicyService.getProductPolicies())
                .thenReturn(responses);

        mockMvc.perform(get(url)
                        .contentType(mapper.writeValueAsString(responses))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].policyNo").value(responseDto.getPolicyNo()))
                .andExpect(jsonPath("$[0].policyMethod").value(responseDto.getPolicyMethod()))
                .andExpect(jsonPath("$[0].saveRate").value(responseDto.getSaveRate()))
                .andExpect(jsonPath("$[0].policySaved").value(responseDto.isPolicySaved()))
                .andDo(print())
                .andDo(document("product-policies-get",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].policyNo").description("?????? ??????"),
                                fieldWithPath("[].policyMethod").description("?????? ??????"),
                                fieldWithPath("[].policySaved").description("?????? ??????"),
                                fieldWithPath("[].saveRate").description("????????? ?????????(%)")
                        )));

        then(productPolicyService).should()
                .getProductPolicies();
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ??????")
    void getProductPolicy() throws Exception {
        when(productPolicyService.getProductPolicyById(anyInt()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{policyNo}", anyInt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNo").value(responseDto.getPolicyNo()))
                .andExpect(jsonPath("$.policyMethod").value(responseDto.getPolicyMethod()))
                .andExpect(jsonPath("$.saveRate").value(responseDto.getSaveRate()))
                .andExpect(jsonPath("$.policySaved").value(responseDto.isPolicySaved()))
                .andDo(print())
                .andDo(document("product-policy-get",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("policyNo").description("?????? ?????? ??????")),
                        responseFields(
                                fieldWithPath("policyNo").description("?????? ??????"),
                                fieldWithPath("policyMethod").description("?????? ??????"),
                                fieldWithPath("policySaved").description("?????? ??????"),
                                fieldWithPath("saveRate").description("????????? ?????????(%)")
                        )
                ));

        then(productPolicyService).should()
                .getProductPolicyById(anyInt());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void modifyProductPolicy() throws Exception {
        // given
        CreateModifyProductPolicyRequestDto modifyDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(modifyDto, "policyMethod", "????????? ??????");
        ReflectionTestUtils.setField(modifyDto, "policySaved", true);
        ReflectionTestUtils.setField(modifyDto, "saveRate", 30);

        // when
        doNothing().when(productPolicyService)
                .modifyProductPolicyById(1, modifyDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(tokenUrl + "/{policyNo}", 1)
                        .content(mapper.writeValueAsString(modifyDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("product-policy-modify-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(parameterWithName("policyNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("policyMethod").description("????????? ?????? ???"),
                                fieldWithPath("policySaved").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("saveRate").description("????????? ?????????(%)")
                        )));

        then(productPolicyService).should()
                .modifyProductPolicyById(anyInt(), any(CreateModifyProductPolicyRequestDto.class));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? (Validation)")
    void modifyProductPolicy_Fail_Test() throws Exception {
        // given
        CreateModifyProductPolicyRequestDto modifyDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(modifyDto, "policyMethod", "10????????? ?????? ????????? ?????????");
        ReflectionTestUtils.setField(modifyDto, "policySaved", true);
        ReflectionTestUtils.setField(modifyDto, "saveRate", 30);

        // when
        doNothing().when(productPolicyService)
                .modifyProductPolicyById(1, modifyDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(tokenUrl + "/{policyNo}", 1)
                        .content(mapper.writeValueAsString(modifyDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("10????????? ?????? ??? ????????????."))
                .andDo(print())
                .andDo(document("product-policy-modify-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("policyNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("policyMethod").description("????????? ?????? ???"),
                                fieldWithPath("policySaved").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("saveRate").description("????????? ?????????(%)")),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? 10????????? ?????? ??? ????????????.")
                        )
                ));
    }
}