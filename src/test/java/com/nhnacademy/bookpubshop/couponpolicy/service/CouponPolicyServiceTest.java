package com.nhnacademy.bookpubshop.couponpolicy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.CreateCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.ModifyCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookpubshop.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookpubshop.couponpolicy.service.impl.CouponPolicyServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * CouponPolicyService 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(CouponPolicyServiceImpl.class)
class CouponPolicyServiceTest {
    @Autowired
    CouponPolicyService couponPolicyService;

    @MockBean
    CouponPolicyRepository couponPolicyRepository;

    ArgumentCaptor<CouponPolicy> captor;
    CouponPolicy couponPolicy;
    GetCouponPolicyResponseDto getCouponPolicyResponseDto;
    CreateCouponPolicyRequestDto createCouponPolicyRequestDto;
    ModifyCouponPolicyRequestDto modifyCouponPolicyRequestDto;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
        getCouponPolicyResponseDto = new GetCouponPolicyResponseDto();
        createCouponPolicyRequestDto = new CreateCouponPolicyRequestDto();
        modifyCouponPolicyRequestDto = new ModifyCouponPolicyRequestDto();
        captor = ArgumentCaptor.forClass(CouponPolicy.class);
    }

    @Test
    @DisplayName("쿠폰정책 등록 성공 테스트")
    void addCouponPolicySuccess_Test() {
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(createCouponPolicyRequestDto, "maxDiscount", 1000L);

        couponPolicyService.addCouponPolicy(createCouponPolicyRequestDto);

        verify(couponPolicyRepository).save(captor.capture());

        CouponPolicy result = captor.getValue();
        assertThat(result.isPolicyFixed()).isEqualTo(createCouponPolicyRequestDto.isPolicyFixed());
        assertThat(result.getPolicyPrice()).isEqualTo(createCouponPolicyRequestDto.getPolicyPrice());
        assertThat(result.getPolicyMinimum()).isEqualTo(createCouponPolicyRequestDto.getPolicyMinimum());
        assertThat(result.getMaxDiscount()).isEqualTo(createCouponPolicyRequestDto.getMaxDiscount());
    }

    @Test
    @DisplayName("쿠폰정책 수정 성공 테스트")
    void modifyCouponPolicySuccess_Test() {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));

        couponPolicyService.modifyCouponPolicy(modifyCouponPolicyRequestDto);

        verify(couponPolicyRepository).findById(anyInt());
    }

    @Test
    @DisplayName("쿠폰정책 수정 실패 테스트_쿠폰정책을 찾을 수 없음")
    void modifyCouponPolicyFail_Test() {
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyFixed", true);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(modifyCouponPolicyRequestDto, "maxDiscount", 1000L);

        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponPolicyService.modifyCouponPolicy(modifyCouponPolicyRequestDto))
                .isInstanceOf(CouponPolicyNotFoundException.class)
                .hasMessageContaining("없는 쿠폰정책번호입니다");
    }

    @Test
    @DisplayName("쿠폰정책 단건 조회 성공 테스트")
    void getCouponPolicySuccess_Test() {
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyNo", 1);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyFixed", true);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "maxDiscount", 1000L);

        when(couponPolicyRepository.findByPolicyNo(anyInt())).thenReturn(Optional.of(getCouponPolicyResponseDto));

        GetCouponPolicyResponseDto result = couponPolicyService.getCouponPolicy(anyInt());

        assertThat(result.getPolicyNo()).isEqualTo(getCouponPolicyResponseDto.getPolicyNo());
        assertThat(result.isPolicyFixed()).isEqualTo(getCouponPolicyResponseDto.isPolicyFixed());
        assertThat(result.getPolicyPrice()).isEqualTo(getCouponPolicyResponseDto.getPolicyPrice());
        assertThat(result.getPolicyMinimum()).isEqualTo(getCouponPolicyResponseDto.getPolicyMinimum());
        assertThat(result.getMaxDiscount()).isEqualTo(getCouponPolicyResponseDto.getMaxDiscount());

        verify(couponPolicyRepository).findByPolicyNo(anyInt());
    }

    @Test
    @DisplayName("쿠폰정책 단건 조회 실패 테스트_쿠폰정책을 찾을 수 없음")
    void getCouponPolicyFail_Test() {
        when(couponPolicyRepository.findByPolicyNo(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponPolicyService.getCouponPolicy(1))
                .isInstanceOf(CouponPolicyNotFoundException.class)
                .hasMessageContaining("없는 쿠폰정책번호입니다");
    }

    @Test
    @DisplayName("쿠폰정책 리스트 조회 성공 테스트")
    void getCouponPolicies() {
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyNo", 1);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyFixed", true);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyPrice", 1000L);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "policyMinimum", 1000L);
        ReflectionTestUtils.setField(getCouponPolicyResponseDto, "maxDiscount", 1000L);

        when(couponPolicyRepository.findByAll()).thenReturn(List.of(getCouponPolicyResponseDto));

        List<GetCouponPolicyResponseDto> result = couponPolicyService.getCouponPolicies();

        assertThat(result.get(0).getPolicyNo()).isEqualTo(getCouponPolicyResponseDto.getPolicyNo());
        assertThat(result.get(0).isPolicyFixed()).isEqualTo(getCouponPolicyResponseDto.isPolicyFixed());
        assertThat(result.get(0).getPolicyPrice()).isEqualTo(getCouponPolicyResponseDto.getPolicyPrice());
        assertThat(result.get(0).getPolicyMinimum()).isEqualTo(getCouponPolicyResponseDto.getPolicyMinimum());
        assertThat(result.get(0).getMaxDiscount()).isEqualTo(getCouponPolicyResponseDto.getMaxDiscount());

        verify(couponPolicyRepository).findByAll();
    }
}