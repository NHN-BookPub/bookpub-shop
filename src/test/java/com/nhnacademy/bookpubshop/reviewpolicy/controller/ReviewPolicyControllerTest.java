package com.nhnacademy.bookpubshop.reviewpolicy.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.CreateReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.ModifyPointReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import com.nhnacademy.bookpubshop.reviewpolicy.service.ReviewPolicyService;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ????????? ?????? ???????????? ??????????????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(ReviewPolicyController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class ReviewPolicyControllerTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @MockBean
    ReviewPolicyService reviewPolicyService;

    String authPath = "/token/review-policies";

    CreateReviewPolicyRequestDto createRequestDto;
    ModifyPointReviewPolicyRequestDto modifyRequestDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        createRequestDto = new CreateReviewPolicyRequestDto();
        modifyRequestDto = new ModifyPointReviewPolicyRequestDto();
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????")
    void reviewPolicyAddTest_Success() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "sendPoint", 100L);

        doNothing().when(reviewPolicyService).createReviewPolicy(createRequestDto);

        mockMvc.perform(post(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review-policy-create",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("sendPoint").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ?????? ??????????????????.")
                        )));

        verify(reviewPolicyService, times(1)).createReviewPolicy(any());
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????_sendPoint??? null??????")
    void reviewPolicyAddTest_Fail_PointIsNull() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "sendPoint", null);

        doNothing().when(reviewPolicyService).createReviewPolicy(createRequestDto);

        mockMvc.perform(post(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-policy-create-pointFail-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("sendPoint").description("????????? ?????????????????? ?????? ??????????????????.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????????????????? ??????????????????.")
                        )));

        verify(reviewPolicyService, times(0)).createReviewPolicy(any());

    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????_sendPoint??? ????????????")
    void reviewPolicyAddTest_Fail_PointIsNegative() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "sendPoint", -100L);

        doNothing().when(reviewPolicyService).createReviewPolicy(createRequestDto);

        mockMvc.perform(post(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-policy-create-pointFail-negative",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("sendPoint").description("????????? ?????????????????? ?????? ??????????????????.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????????????????? 0??? ????????????????????????.")
                        )));

        verify(reviewPolicyService, times(0)).createReviewPolicy(any());
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????")
    void reviewPolicyModifyPoint_Success() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "sendPoint", 100L);
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);

        doNothing().when(reviewPolicyService).modifyReviewPolicy(modifyRequestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review-policy-modify-point",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("????????? ??????????????? ???????????????."),
                                fieldWithPath("sendPoint").description("????????? ?????????????????? ?????? ??????????????????.")
                        )));

        verify(reviewPolicyService, times(1)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????_policyNo??? null??????")
    void reviewPolicyModifyPoint_Fail_PolicyNoIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "sendPoint", 100L);
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", null);

        doNothing().when(reviewPolicyService).modifyReviewPolicy(modifyRequestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-policy-modify-policyNo-failNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("????????? ??????????????? ???????????????."),
                                fieldWithPath("sendPoint").description("????????? ?????????????????? ?????? ??????????????????.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? ?????? ????????? ???????????????")
                        )));

        verify(reviewPolicyService, times(0)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????_sendPoint??? null??????")
    void reviewPolicyModifyPoint_Fail_PointIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "sendPoint", null);
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);

        doNothing().when(reviewPolicyService).modifyReviewPolicy(modifyRequestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-policy-modify-point-failNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("????????? ??????????????? ???????????????."),
                                fieldWithPath("sendPoint").description("????????? ?????????????????? ?????? ??????????????????.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ???????????????????????? ???????????????")
                        )));

        verify(reviewPolicyService, times(0)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????_sendPoint??? ????????????")
    void reviewPolicyModifyPoint_Fail_PointIsNegative() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "sendPoint", -100L);
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);

        doNothing().when(reviewPolicyService).modifyReviewPolicy(modifyRequestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-policy-modify-point-failNegative",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("????????? ??????????????? ???????????????."),
                                fieldWithPath("sendPoint").description("????????? ?????????????????? ?????? ??????????????????.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????????????????? 0??? ????????????????????????")
                        )));

        verify(reviewPolicyService, times(0)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????? ?????? ?????? ?????????")
    void reviewPolicyModifyUsed_Success() throws Exception {
        doNothing().when(reviewPolicyService).modifyUsedReviewPolicy(1);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{policyNo}/used", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review-policy-modify-used",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("policyNo").description("????????? ??????????????? ???????????????.")
                        )));

        verify(reviewPolicyService, times(1)).modifyUsedReviewPolicy(anyInt());
    }

    @Test
    @DisplayName("??????????????? ????????? ?????? ?????? ?????????")
    void reviewPolicyList() throws Exception {
        GetReviewPolicyResponseDto dto = new GetReviewPolicyResponseDto(1, 100L, true);

        when(reviewPolicyService.getReviewPolicies()).thenReturn(List.of(dto));

        mockMvc.perform(get(authPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].policyNo").value(dto.getPolicyNo()))
                .andExpect(jsonPath("$[0].sendPoint").value(dto.getSendPoint()))
                .andDo(document("get-review-policies",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].policyNo").description("????????? ????????? ?????????????????????."),
                                fieldWithPath("[].sendPoint").description("????????? ?????? ????????????????????????."),
                                fieldWithPath("[].policyUsed").description("?????? ?????? ???????????????.")
                        )));
    }
}