package com.nhnacademy.bookpubshop.coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * CouponController 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponService couponService;

    ObjectMapper mapper;
    String uri = "/api/coupons";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("쿠폰 전체 리스트 조회 api 테스트")
    void couponList() throws Exception {
        // given
        GetCouponResponseDto response = new GetCouponResponseDto(1L, "member", "template", "image", true,
                10L, 1000L, 1000L, LocalDateTime.now(), true);
        PageRequest pageable = PageRequest.of(0, 10);
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(response), pageable, 1);

        // when
        when(couponService.getCoupons(pageable, null, null))
                .thenReturn(page);

        // then
        mockMvc.perform(get(uri)
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].couponNo").value(response.getCouponNo()))
                .andExpect(jsonPath("$.content[0].memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.content[0].templateName").value(response.getTemplateName()))
                .andExpect(jsonPath("$.content[0].templateImage").value(response.getTemplateImage()))
                .andExpect(jsonPath("$.content[0].policyFixed").value(response.isPolicyFixed()))
                .andExpect(jsonPath("$.content[0].policyPrice").value(response.getPolicyPrice()))
                .andExpect(jsonPath("$.content[0].policyMinimum").value(response.getPolicyMinimum()))
                .andExpect(jsonPath("$.content[0].maxDiscount").value(response.getMaxDiscount()))
                .andExpect(jsonPath("$.content[0].couponUsed").value(response.isCouponUsed()))
                .andDo(print());

        then(couponService).should()
                .getCoupons(any(), any(), any());
    }

    @Test
    @DisplayName("단건 쿠폰을 조회 api 테스트")
    void couponDetail_Test() throws Exception {
        // given
        GetCouponResponseDto response = new GetCouponResponseDto(1L, "member", "template", "image", true,
                10L, 1000L, 1000L, LocalDateTime.now(), true);

        // when
        when(couponService.getCoupon(anyLong()))
                .thenReturn(response);

        // then
        mockMvc.perform(get(uri + "/{couponNo}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(couponService).should()
                .getCoupon(anyLong());
    }

    @Test
    @DisplayName("쿠폰 생성 api 테스트")
    void AddCoupon_Test() throws Exception {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberId", "idId");

        // when
        doNothing().when(couponService).createCoupon(request);

        // then
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(couponService).should()
                .createCoupon(any(CreateCouponRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰 생성 api templateNo Validation 테스트")
    void addCoupon_NullTemplateNo_Validation_Test() throws Exception {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", null);
        ReflectionTestUtils.setField(request, "memberId", "idId");

        // when
        doNothing().when(couponService).createCoupon(request);

        // then
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰템플릿 번호를 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 생성 api memberNo Validation 테스트")
    void addCoupon_NullMemberId_Validation_Test() throws Exception {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberId", null);

        // when
        doNothing().when(couponService).createCoupon(request);

        // then
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("멤버 아이디를 입력해주세요."))
                .andDo(print());
    }


    @Test
    @DisplayName("쿠폰 사용여부 수정 api 테스트")
    void modifyCouponUsed_Test() throws Exception {
        // when
        doNothing().when(couponService).modifyCouponUsed(anyLong());

        // then
        mockMvc.perform(put(uri + "/{couponNo}" + "/used", 1L))
                .andExpect(status().is2xxSuccessful());

        then(couponService).should()
                .modifyCouponUsed(any(Long.class));
    }
}