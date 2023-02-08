package com.nhnacademy.bookpubshop.tier.relationship.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.TierNotFoundException;
import com.nhnacademy.bookpubshop.tier.relationship.dto.request.CreateTierCouponRequestDto;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import com.nhnacademy.bookpubshop.tier.relationship.dummy.TierCouponDummy;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import com.nhnacademy.bookpubshop.tier.relationship.exception.NotFoundTierCouponException;
import com.nhnacademy.bookpubshop.tier.relationship.repository.TierCouponRepository;
import com.nhnacademy.bookpubshop.tier.relationship.service.impl.TierCouponServiceImpl;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 등급 쿠폰 Service 테스트.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(TierCouponServiceImpl.class)
class TierCouponServiceTest {

    TierCouponService tierCouponService;
    @MockBean
    TierCouponRepository tierCouponRepository;

    @MockBean
    TierRepository tierRepository;

    @MockBean
    CouponTemplateRepository couponTemplateRepository;
    TierCoupon tierCoupon;

    CouponTemplate couponTemplate;

    BookPubTier tier;

    CouponPolicy couponPolicy;

    CouponType couponType;
    Product product;
    Category category;
    CouponStateCode couponStateCode;

    GetTierCouponResponseDto getTierCouponResponseDto;

    CreateTierCouponRequestDto createTierCouponRequestDto;

    ArgumentCaptor<TierCoupon> captor;

    @BeforeEach
    void setUp() {
        tierCouponService = new TierCouponServiceImpl(tierCouponRepository, tierRepository,
                couponTemplateRepository);
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        category = CategoryDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category,
                couponStateCode);
        tier = TierDummy.dummy();
        tierCoupon = TierCouponDummy.dummy(couponTemplate, tier);

        getTierCouponResponseDto = new GetTierCouponResponseDto(1L, "깜짝쿠폰", 1, "white");
        createTierCouponRequestDto = new CreateTierCouponRequestDto();
        ReflectionTestUtils.setField(createTierCouponRequestDto, "templateNo", 1L);
        ReflectionTestUtils.setField(createTierCouponRequestDto, "tierNo", 1);
        captor = ArgumentCaptor.forClass(TierCoupon.class);
    }

    @Test
    @DisplayName("등급 쿠폰 전체 조회 테스트")
    void findTierCouponsTest() {

        List<GetTierCouponResponseDto> responses = new ArrayList<>();
        responses.add(getTierCouponResponseDto);

        Pageable pageable = Pageable.ofSize(10);
        Page<GetTierCouponResponseDto> page = PageableExecutionUtils.getPage(responses, pageable,
                () -> 1L);

        //when
        when(tierCouponRepository.findAllBy(pageable)).thenReturn(page);

        //then
        assertThat(tierCouponService.getTierCoupons(pageable)).isEqualTo(page);
        assertThat(tierCouponService.getTierCoupons(pageable).getContent().get(0)
                .getTierNo()).isEqualTo(responses.get(0).getTierNo());
        assertThat(tierCouponService.getTierCoupons(pageable).getContent().get(0)
                .getTemplateNo()).isEqualTo(responses.get(0).getTemplateNo());
    }

    @Test
    @DisplayName("등급 번호로 등급 쿠폰리스트 조회 테스트")
    void findTierCouponsByTierNoTest() {
        List<Long> responses = new ArrayList<>();
        responses.add(1L);

        // when
        when(tierCouponRepository.findAllByTierNo(1)).thenReturn(responses);

        // then
        assertThat(tierCouponService.getTierCouponsByTierNo(1)).isEqualTo(responses);

        verify(tierCouponRepository, times(1)).findAllByTierNo(anyInt());
    }

    @Test
    @DisplayName("등급 쿠폰 생성 성공 테스트")
    void createTierCouponTest() {

        when(tierRepository.findById(anyInt())).thenReturn(Optional.ofNullable(tier));
        when(couponTemplateRepository.findById(anyLong())).thenReturn(
                Optional.ofNullable(couponTemplate));
        when(tierCouponRepository.save(any())).thenReturn(tierCoupon);

        tierCouponService.createTierCoupon(createTierCouponRequestDto);

        verify(tierCouponRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("등급이 존재 하지 않아 등급 쿠폰 생성 실패 테스트")
    void createTierCouponCauseTierNoFailTest() {
        //when
        when(tierRepository.findById(anyInt())).thenThrow(TierNotFoundException.class);

        //then
        assertThatThrownBy(() -> tierCouponService.createTierCoupon(createTierCouponRequestDto))
                .isInstanceOf(TierNotFoundException.class);

    }

    @Test
    @DisplayName("쿠폰 템플릿이 존재하지 않아  등급 쿠폰 생성 실패 테스트")
    void createTierCouponCauseTemplateNoFailTest() {

        //when
        when(tierRepository.findById(anyInt())).thenReturn(Optional.ofNullable(tier));
        when(couponTemplateRepository.findById(anyLong())).thenThrow(
                CouponTemplateNotFoundException.class);

        //then
        assertThatThrownBy(() -> tierCouponService.createTierCoupon(createTierCouponRequestDto))
                .isInstanceOf(CouponTemplateNotFoundException.class);
    }

    @Test
    @DisplayName("등급 쿠폰 삭제 성공 테스트")
    void deleteTierCouponTest() {
        //when
        when(tierCouponRepository.existsById(tierCoupon.getPk())).thenReturn(true);

        //then
        tierCouponService.deleteTierCoupon(1L, 1);

        verify(tierCouponRepository, times(1)).deleteById(tierCoupon.getPk());

    }

    @Test
    @DisplayName("등급 쿠폰 존재 하지 않아 삭제 실패 테스트")
    void deleteTierCouponFailTest() {
        //when
        when(tierCouponRepository.existsById(tierCoupon.getPk())).thenReturn(false);

        //then
        assertThatThrownBy(() -> tierCouponService.deleteTierCoupon(1L, 2))
                .isInstanceOf(NotFoundTierCouponException.class);

        verify(tierCouponRepository, times(0)).deleteById(tierCoupon.getPk());

    }

}