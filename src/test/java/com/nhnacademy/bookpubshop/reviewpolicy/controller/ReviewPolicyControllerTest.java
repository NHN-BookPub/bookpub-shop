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
 * 상품평 정책 컨트롤러 테스트입니다.
 *
 * @author : 정유진
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
    @DisplayName("상품평정책 등록 성공 테스트")
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
                                fieldWithPath("sendPoint").type(JsonFieldType.NUMBER).description("생성할 상품평정책의 지급 포인트입니다.")
                        )));

        verify(reviewPolicyService, times(1)).createReviewPolicy(any());
    }

    @Test
    @DisplayName("상품평정책 등록 실패 테스트_sendPoint가 null일때")
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
                                fieldWithPath("sendPoint").description("생성할 상품평정책의 지급 포인트입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("리뷰지급포인트를 입력해주세요.")
                        )));

        verify(reviewPolicyService, times(0)).createReviewPolicy(any());

    }

    @Test
    @DisplayName("상품평정책 등록 실패 테스트_sendPoint가 음수일때")
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
                                fieldWithPath("sendPoint").description("생성할 상품평정책의 지급 포인트입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("지급포인트는 0원 이상이어야합니다.")
                        )));

        verify(reviewPolicyService, times(0)).createReviewPolicy(any());
    }

    @Test
    @DisplayName("상품평정책 수정 성공 테스트")
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
                                fieldWithPath("policyNo").description("수정할 상품평정책 번호입니다."),
                                fieldWithPath("sendPoint").description("생성할 상품평정책의 지급 포인트입니다.")
                        )));

        verify(reviewPolicyService, times(1)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("상품평정책 수정 실패 테스트_policyNo가 null일때")
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
                                fieldWithPath("policyNo").description("수정할 상품평정책 번호입니다."),
                                fieldWithPath("sendPoint").description("생성할 상품평정책의 지급 포인트입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("수정할 상품평 정책 번호를 입력하세요")
                        )));

        verify(reviewPolicyService, times(0)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("상품평정책 수정 실패 테스트_sendPoint가 null일때")
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
                                fieldWithPath("policyNo").description("수정할 상품평정책 번호입니다."),
                                fieldWithPath("sendPoint").description("생성할 상품평정책의 지급 포인트입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("수정할 리뷰지급포인트를 입력하세요")
                        )));

        verify(reviewPolicyService, times(0)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("상품평정책 수정 실패 테스트_sendPoint가 음수일때")
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
                                fieldWithPath("policyNo").description("수정할 상품평정책 번호입니다."),
                                fieldWithPath("sendPoint").description("생성할 상품평정책의 지급 포인트입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("지급포인트는 0원 이상이어야합니다")
                        )));

        verify(reviewPolicyService, times(0)).modifyReviewPolicy(any());
    }

    @Test
    @DisplayName("상품평정책 현재 사용 여부 수정 성공 테스트")
    void reviewPolicyModifyUsed_Success() throws Exception {
        doNothing().when(reviewPolicyService).modifyUsedReviewPolicy(1);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{policyNo}/used", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review-policy-modify-used",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("policyNo").description("수정할 상품평정책 번호입니다.")
                        )));

        verify(reviewPolicyService, times(1)).modifyUsedReviewPolicy(anyInt());
    }

    @Test
    @DisplayName("상품평정책 리스트 조회 성공 테스트")
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
                                fieldWithPath("[].policyNo").description("조회된 상품평 정책번호입니다."),
                                fieldWithPath("[].sendPoint").description("상품평 정책 지급포인트입니다."),
                                fieldWithPath("[].policyUsed").description("현재 사용 여부입니다.")
                        )));
    }
}