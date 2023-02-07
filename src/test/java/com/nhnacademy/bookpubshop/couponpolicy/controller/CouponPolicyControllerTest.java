package com.nhnacademy.bookpubshop.couponpolicy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.CreateCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.ModifyCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
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
 * CouponPolicyController 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponPolicyController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CouponPolicyControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @MockBean
    CouponPolicyService couponPolicyService;
    String authPath = "/token/coupon-policies";

    CreateCouponPolicyRequestDto createCouponPolicyRequestDto;
    ModifyCouponPolicyRequestDto modifyCouponPolicyRequestDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        createCouponPolicyRequestDto = new CreateCouponPolicyRequestDto();
        modifyCouponPolicyRequestDto = new ModifyCouponPolicyRequestDto();
    }

    @Test
    @DisplayName("쿠폰정책 생성 성공 테스트")
    void couponPolicyAddSuccess_Test() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("coupon-policy-add-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("할인적용 최소 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 policyPrice 컬럼이 null일 때")
    void couponPolicyAddFail_Test_null() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", null);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-add-policyPriceFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("할인적용 최소 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("값을 기입하셔야 합니다.")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 policyMinimum 컬럼이 null일 때")
    void couponPolicyAddFail2_Test_null() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", null);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-add-policyMinimumFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("할인적용 최소 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("값을 기입하셔야 합니다.")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_policyPrice 가 음수일 때")
    void couponPolicyAddFail_Test_DiscountRate() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", -10L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-add-policyPrice-negativeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("할인적용 최소 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("0 이상의 값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_policyMinimum 가 음수일 때")
    void couponPolicyAddFail_Test_PolicyMinimum() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", -10L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-add-policyMinimum-negativeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("할인적용 최소 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("0 이상의 값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_maxDiscount 가 음수일 때")
    void couponPolicyAddFail_Test_MaxDiscount() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", -10L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-add-maxDiscount-negativeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("할인적용 최소 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("0 이상의 값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 수정 성공 테스트")
    void couponPolicyModifySuccess_Test() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("coupon-policy-modify-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        )));

        then(couponPolicyService).should().modifyCouponPolicy(any(ModifyCouponPolicyRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_policyPrice 컬럼이 null 일 때")
    void couponPolicyModifyFail_Test_null() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", null);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-modify-policyPriceFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_policyMinimum 컬럼이 null 일 때")
    void couponPolicyModifyFail_Test2_null() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", null);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-modify-policyMinimumFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_policyPrice 가 음수일 때")
    void couponPolicyModifyFail_Test_discountRate() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", -10L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-modify-policyPrice-negativeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("0 이상의 값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_policyMinimum 가 음수일 때")
    void couponPolicyModifyFail_Test_policyMinimum() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", -10L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-modify-policyMinimum-negativeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("0 이상의 값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_maxDiscount 가 음수일 때")
    void couponPolicyModifyFail_Test_maxDiscount() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", -10L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."))
                .andDo(document("coupon-policy-modify-maxDiscount-negativeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("0 이상의 값을 기입하여야 합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_정책번호를 기입하지 않았을 때")
    void couponPolicyModifyFail_Test_PolicyNo() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", null);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("정책번호를 기입하여야합니다."))
                .andDo(document("coupon-policy-modify-policyNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("policyNo").description("정책 번호"),
                                fieldWithPath("policyFixed").description("수정할 정액 여부"),
                                fieldWithPath("policyPrice").description("수정할 할인 가격"),
                                fieldWithPath("policyMinimum").description("수정할 최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("수정할 최대 할인 금액")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정책번호를 기입하여야합니다.")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 단건 조회 테스트")
    void couponPolicyDetailTest() throws Exception {
        GetCouponPolicyResponseDto dto =
                new GetCouponPolicyResponseDto(1, true, 1000L, 1000L, 1000L);

        given(couponPolicyService.getCouponPolicy(anyInt())).willReturn(dto);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(authPath + "/{policyNo}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.policyNo", equalTo(dto.getPolicyNo())))
                .andExpect(jsonPath("$.policyFixed", equalTo(dto.isPolicyFixed())))
                .andExpect(jsonPath("$.policyPrice", is(dto.getPolicyPrice()), Long.class))
                .andExpect(jsonPath("$.policyMinimum", is(dto.getPolicyMinimum()), Long.class))
                .andExpect(jsonPath("$.maxDiscount", is(dto.getMaxDiscount()), Long.class))
                .andDo(document("coupon-policy-get",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("policyNo").description("정책 번호")),
                        responseFields(
                                fieldWithPath("policyNo").description("상품 번호"),
                                fieldWithPath("policyFixed").description("정액 여부"),
                                fieldWithPath("policyPrice").description("할인 가격"),
                                fieldWithPath("policyMinimum").description("최소 주문 금액"),
                                fieldWithPath("maxDiscount").description("최대 할인 금액")
                        )));
    }

    @Test
    @DisplayName("쿠폰정책 리스트 조회 테스트")
    void couponPolicyListTest() throws Exception {
        GetCouponPolicyResponseDto dto =
                new GetCouponPolicyResponseDto(1, true, 1000L, 1000L, 1000L);
        GetCouponPolicyResponseDto tmp =
                new GetCouponPolicyResponseDto(2, true, 2000L, 2000L, 2000L);

        given(couponPolicyService.getCouponPolicies()).willReturn(List.of(dto, tmp));

        //when && then
        mockMvc.perform(get(authPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].policyNo", equalTo(dto.getPolicyNo())))
                .andExpect(jsonPath("$[0].policyFixed", equalTo(dto.isPolicyFixed())))
                .andExpect(jsonPath("$[0].policyPrice", is(dto.getPolicyPrice()), Long.class))
                .andExpect(jsonPath("$[0].policyMinimum", is(dto.getPolicyMinimum()), Long.class))
                .andExpect(jsonPath("$[0].maxDiscount", is(dto.getMaxDiscount()), Long.class))
                .andDo(document("coupon-policies-get",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].policyNo").description("상품 번호"),
                                fieldWithPath("[].policyFixed").description("정액 여부"),
                                fieldWithPath("[].policyPrice").description("할인 가격"),
                                fieldWithPath("[].policyMinimum").description("최소 주문 금액"),
                                fieldWithPath("[].maxDiscount").description("최대 할인 금액")
                        )));
    }

}