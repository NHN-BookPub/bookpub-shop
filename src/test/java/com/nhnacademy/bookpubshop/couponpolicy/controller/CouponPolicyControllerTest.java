package com.nhnacademy.bookpubshop.couponpolicy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
class CouponPolicyControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @MockBean
    CouponPolicyService couponPolicyService;

    String path = "/api/coupon-policies";

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
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());


    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_NotNull 컬럼이 null일 때")
    void couponPolicyAddFail_Test_null() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "discountRate", null);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", null);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_discountRate 가 음수일 때")
    void couponPolicyAddFail_Test_DiscountRate() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "discountRate", -10L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_policyMinimum 가 음수일 때")
    void couponPolicyAddFail_Test_PolicyMinimum() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", -10L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 생성 validation 검증 실패 테스트_maxDiscount 가 음수일 때")
    void couponPolicyAddFail_Test_MaxDiscount() throws Exception {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", -10L);

        doNothing().when(couponPolicyService).addCouponPolicy(createCouponPolicyRequestDto);

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 수정 성공 테스트")
    void couponPolicyModifySuccess_Test() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(path)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        then(couponPolicyService).should().modifyCouponPolicy(any(ModifyCouponPolicyRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_Not null 컬럼이 null 일 때")
    void couponPolicyModifyFail_Test_null() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "discountRate", null);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", null);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(path)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_discountRate 가 음수일 때")
    void couponPolicyModifyFail_Test_discountRate() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "discountRate", -10L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(path)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_policyMinimum 가 음수일 때")
    void couponPolicyModifyFail_Test_policyMinimum() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", -10L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(path)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_maxDiscount 가 음수일 때")
    void couponPolicyModifyFail_Test_maxDiscount() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", -10L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(path)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0 이상의 값을 기입하여야 합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 수정 validation 검증 실패 테스트_정책번호를 기입하지 않았을 때")
    void couponPolicyModifyFail_Test_PolicyNo() throws Exception {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", null);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "discountRate", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        doNothing().when(couponPolicyService).modifyCouponPolicy(modifyCouponPolicyRequestDto);

        mockMvc.perform(put(path)
                        .content(objectMapper.writeValueAsString(modifyCouponPolicyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("정책번호를 기입하여야합니다."));
    }

    @Test
    @DisplayName("쿠폰정책 단건 조회 테스트")
    void couponPolicyDetailTest() throws Exception {
        GetCouponPolicyResponseDto dto =
                new GetCouponPolicyResponseDto(1, true, 1000L, 1000L, 1000L);

        given(couponPolicyService.getCouponPolicy(anyInt())).willReturn(dto);

        //when && then
        mockMvc.perform(get(path + "/{policyNo}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.policyNo", equalTo(dto.getPolicyNo())))
                .andExpect(jsonPath("$.policyFixed", equalTo(dto.isPolicyFixed())))
                .andExpect(jsonPath("$.discountRate", is(dto.getDiscountRate()), Long.class))
                .andExpect(jsonPath("$.policyMinimum", is(dto.getPolicyMinimum()), Long.class))
                .andExpect(jsonPath("$.maxDiscount", is(dto.getMaxDiscount()), Long.class));
    }

    @Test
    @DisplayName("쿠폰정책 리스트 조회 테스트")
    void couponPolicyListTest() throws Exception {
        GetCouponPolicyResponseDto dto =
                new GetCouponPolicyResponseDto(1, true, 1000L, 1000L, 1000L);

        given(couponPolicyService.getCouponPolicies()).willReturn(List.of(dto));

        //when && then
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].policyNo", equalTo(dto.getPolicyNo())))
                .andExpect(jsonPath("$[0].policyFixed", equalTo(dto.isPolicyFixed())))
                .andExpect(jsonPath("$[0].discountRate", is(dto.getDiscountRate()), Long.class))
                .andExpect(jsonPath("$[0].policyMinimum", is(dto.getPolicyMinimum()), Long.class))
                .andExpect(jsonPath("$[0].maxDiscount", is(dto.getMaxDiscount()), Long.class));
    }

}