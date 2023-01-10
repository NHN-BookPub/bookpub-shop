package com.nhnacademy.bookpubshop.coupontype.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.coupontype.exception.CouponTypeNotFoundException;
import com.nhnacademy.bookpubshop.coupontype.repository.CouponTypeRepository;
import com.nhnacademy.bookpubshop.coupontype.service.impl.CouponTypeServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * CouponTypeService 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(CouponTypeServiceImpl.class)
class CouponTypeServiceTest {
    @Autowired
    CouponTypeService couponTypeService;

    @MockBean
    CouponTypeRepository couponTypeRepository;

    GetCouponTypeResponseDto getCouponResponseDto;
    CouponType couponType;

    @BeforeEach
    void setUp() {
        getCouponResponseDto = new GetCouponTypeResponseDto();
        couponType = CouponTypeDummy.dummy();
    }

    @Test
    @DisplayName("쿠폰유형 단건 조회 성공 테스트")
    void getCouponTypeSuccessTest() {
        //given
        ReflectionTestUtils.setField(getCouponResponseDto, "typeNo", 1L);
        ReflectionTestUtils.setField(getCouponResponseDto, "typeName", "일반");

        //when
        when(couponTypeRepository.findByTypeNo(anyLong()))
                .thenReturn(Optional.of(getCouponResponseDto));

        //then
        GetCouponTypeResponseDto result = couponTypeService.getCouponType(anyLong());
        assertThat(result.getTypeNo()).isEqualTo(getCouponResponseDto.getTypeNo());
        assertThat(result.getTypeName()).isEqualTo(getCouponResponseDto.getTypeName());

        verify(couponTypeRepository).findByTypeNo(anyLong());
    }

    @Test
    @DisplayName("쿠폰유형 조회 실패 테스트")
    void getCouponTypeFailTest() {
        //when
        when(couponTypeRepository.findByTypeNo(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponTypeService.getCouponType(1L))
                .isInstanceOf(CouponTypeNotFoundException.class)
                .hasMessageContaining("Not Found CouponType");
    }

    @Test
    @DisplayName("쿠폰유형 리스트 조회 성공 테스트")
    void getCouponTypesSuccessTest() {
        //given
        ReflectionTestUtils.setField(getCouponResponseDto, "typeNo", 1L);
        ReflectionTestUtils.setField(getCouponResponseDto, "typeName", "일반");

        //when
        when(couponTypeRepository.findAllBy()).thenReturn(List.of(getCouponResponseDto));

        //then
        List<GetCouponTypeResponseDto> result = couponTypeService.getCouponTypes();
        assertThat(result.get(0).getTypeNo()).isEqualTo(getCouponResponseDto.getTypeNo());
        assertThat(result.get(0).getTypeName()).isEqualTo(getCouponResponseDto.getTypeName());

        verify(couponTypeRepository).findAllBy();
    }

}