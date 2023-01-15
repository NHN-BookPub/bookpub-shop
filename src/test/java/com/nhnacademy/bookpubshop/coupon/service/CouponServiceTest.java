package com.nhnacademy.bookpubshop.coupon.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.request.ModifyCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dummy.CouponDummy;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.CouponNotFoundException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.coupon.service.impl.CouponServiceImpl;
import com.nhnacademy.bookpubshop.couponmonth.dummy.CouponMonthDummy;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
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
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.MemberNotFoundException;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * CouponService 테스트
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(CouponServiceImpl.class)
class CouponServiceTest {

    @Autowired
    CouponService couponService;

    @MockBean
    CouponRepository couponRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    CouponTemplateRepository couponTemplateRepository;

    ArgumentCaptor<Coupon> captor;

    CouponPolicy couponPolicy;
    CouponType couponType;
    CouponStateCode couponStateCode;
    CouponTemplate couponTemplate;
    CouponMonth couponMonth;
    ProductPolicy productPolicy;
    Category category;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Product product;
    File file;
    BookPubTier tier;
    Member member;
    Coupon coupon;
    BookpubOrder bookpubOrder;
    OrderProduct orderProduct;
    PricePolicy pricePolicy;
    PricePolicy packagePolicy;
    Address address;
    OrderStateCode orderStateCode;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        category = CategoryDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category, couponStateCode);
        couponMonth = CouponMonthDummy.dummy(couponTemplate);

        file = FileDummy.dummy(null, null, couponTemplate, product, null);
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        address = AddressDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        bookpubOrder = OrderDummy.dummy(member, pricePolicy, packagePolicy, address, orderStateCode);

        coupon = CouponDummy.dummy(couponTemplate, bookpubOrder, orderProduct, member);

        captor = ArgumentCaptor.forClass(Coupon.class);
    }

    @Test
    @DisplayName("쿠폰 생성 성공 테스트")
    void createCoupon_Success_Test() {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberNo", 1L);

        // when
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));

        // then
        couponService.createCoupon(request);

        verify(memberRepository, times(1))
                .findById(anyLong());
        verify(couponTemplateRepository, times(1))
                .findById(anyLong());
        verify(couponRepository, times(1))
                .save(captor.capture());
    }

    @Test
    @DisplayName("쿠폰 생성 실패 테스트 (member 못 찾은 경우)")
    void createCoupon_Fail_MemberNotFound_Test() {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberNo", 1L);

        // when
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.createCoupon(request))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 생성 실패 테스트 (couponTemplate 못 찾은 경우)")
    void createCoupon_Fail_CouponTemplateNotFound_Test() {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberNo", 1L);

        // when
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.createCoupon(request))
                .isInstanceOf(CouponTemplateNotFoundException.class)
                .hasMessageContaining(CouponTemplateNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 수정 성공 테스트 (사용시간 변경)")
    void modifyCoupon_Success_ChangeTime_Test() {
        // given
        ModifyCouponRequestDto request = new ModifyCouponRequestDto();
        ReflectionTestUtils.setField(request, "couponNo", 1L);
        ReflectionTestUtils.setField(request, "couponUsed", true);

        // when
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));

        // then
        couponService.modifyCouponUsed(request);

        verify(couponRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("쿠폰 수정 성공 테스트 (사용시간 변경 X)")
    void modifyCoupon_Success_Test() {
        // given
        ModifyCouponRequestDto request = new ModifyCouponRequestDto();
        ReflectionTestUtils.setField(request, "couponNo", 1L);
        ReflectionTestUtils.setField(request, "couponUsed", false);

        // when
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));

        // then
        couponService.modifyCouponUsed(request);

        verify(couponRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("쿠폰 수정 실패 테스트 (coupon 못 찾은 경우)")
    void modifyCoupon_Fail_NotFoundCoupon_Test() {
        // given
        ModifyCouponRequestDto request = new ModifyCouponRequestDto();
        ReflectionTestUtils.setField(request, "couponNo", 1L);
        ReflectionTestUtils.setField(request, "couponUsed", false);

        // when
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.modifyCouponUsed(request))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining(CouponNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 조회 성공 테스트")
    void getCoupon_Success_Test() {
        // given
        GetCouponResponseDto response =
                new GetCouponResponseDto(1L, "member", "template", "image", true, 1L, 10L, 100L, LocalDateTime.now(), true);

        // when
        when(couponRepository.getCoupon(anyLong()))
                .thenReturn(Optional.of(response));

        // then
        couponService.getCoupon(anyLong());

        verify(couponRepository, times(1))
                .getCoupon(anyLong());
    }

    @Test
    @DisplayName("쿠폰 조회 실패 테스트 (coupon 못 찾은 경우)")
    void getCoupon_Fail_NotFoundCoupon_Test() {
        // given

        // when
        when(couponRepository.getCoupon(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.getCoupon(anyLong()))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining(CouponNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 전체 조회 테스트")
    void getCoupons_Success_Test() {
        // given
        GetCouponResponseDto response =
                new GetCouponResponseDto(1L, "member", "template", "image", true, 1L, 10L, 100L, LocalDateTime.now(), true);
        Pageable pageable = Pageable.ofSize(10);
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(response), pageable, 1);

        // when
        when(couponRepository.getCoupons(pageable))
                .thenReturn(page);

        // then
        couponService.getCoupons(pageable);

        verify(couponRepository, times(1))
                .getCoupons(pageable);
    }

}