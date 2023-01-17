package com.nhnacademy.bookpubshop.couponstatecode.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import com.nhnacademy.bookpubshop.couponstatecode.exception.CouponStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.couponstatecode.repository.CouponStateCodeRepository;
import com.nhnacademy.bookpubshop.couponstatecode.service.impl.CouponStateCodeServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 쿠폰상태코드 service 테스트
 *
 * @author : 정유진
 * @since : 1.0
 **/

@ExtendWith(SpringExtension.class)
@Import(CouponStateCodeServiceImpl.class)
class CouponStateCodeServiceTest {

    @Autowired
    private CouponStateCodeService couponStateCodeService;

    @MockBean
    private CouponStateCodeRepository couponStateCodeRepository;

    @Test
    @DisplayName("쿠폰상태코드 get 테스트")
    void getCouponStateCode() {
        GetCouponStateCodeResponseDto dto =
                new GetCouponStateCodeResponseDto();
        ReflectionTestUtils.setField(dto, "codeNo", 1);
        ReflectionTestUtils.setField(dto, "codeTarget", "test_target");


        given(couponStateCodeRepository.findByCodeNoAndCodeUsedTrue(anyInt())).willReturn(Optional.of(dto));

        GetCouponStateCodeResponseDto result = couponStateCodeService.getCouponStateCode(anyInt());

        assertThat(result.getCodeNo()).isEqualTo(dto.getCodeNo());
        assertThat(result.getCodeTarget()).isEqualTo(dto.getCodeTarget());

        verify(couponStateCodeRepository).findByCodeNoAndCodeUsedTrue(anyInt());
    }

    @Test
    @DisplayName("쿠폰상태코드 get 예외 발생_Not Found")
    void getCouponStateCode_NotFoundException() {
        given(couponStateCodeRepository.findByCodeNoAndCodeUsedTrue(1))
                .willThrow(new CouponStateCodeNotFoundException(1));

        assertThatThrownBy(() -> couponStateCodeService.getCouponStateCode(1))
                .isInstanceOf(CouponStateCodeNotFoundException.class)
                .hasMessageContaining("Not Found CouponStateCode");

        verify(couponStateCodeRepository).findByCodeNoAndCodeUsedTrue(1);
    }

    @Test
    @DisplayName("쿠폰상태코드 리스트 get 테스트")
    void getCouponStateCodes() {
        given(couponStateCodeRepository.findAllByCodeUsedTrue()).willReturn(List.of(
                new GetCouponStateCodeResponseDto(1, "test_target_one"),
                new GetCouponStateCodeResponseDto(2, "test_target_two")
        ));

        List<GetCouponStateCodeResponseDto> result = couponStateCodeService.getCouponStateCodes();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCodeNo()).isEqualTo(1);
        assertThat(result.get(0).getCodeTarget()).isEqualTo("test_target_one");

        verify(couponStateCodeRepository).findAllByCodeUsedTrue();
    }
}