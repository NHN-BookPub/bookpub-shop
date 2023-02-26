package com.nhnacademy.bookpubshop.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dummy.CouponDummy;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.CouponNotFoundException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.coupon.service.impl.CouponServiceImpl;
import com.nhnacademy.bookpubshop.couponmonth.dummy.CouponMonthDummy;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.repository.CouponMonthRepository;
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
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.point.dummy.PointHistoryDummy;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    CouponService couponService;
    @MockBean
    CouponRepository couponRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    CouponTemplateRepository couponTemplateRepository;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    FileManagement fileManagement;

    @MockBean
    CouponMonthRepository couponMonthRepository;
    @MockBean
    PointHistoryRepository pointHistoryRepository;
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
    OrderStateCode orderStateCode;
    GetCouponResponseDto getCouponResponseDto;
    GetOrderCouponResponseDto orderCouponResponseDto;
    PointHistory pointHistory;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        getCouponResponseDto = CouponDummy.getCouponResponseDtoDummy();
        couponService = new CouponServiceImpl(couponRepository, memberRepository,
                couponTemplateRepository, productRepository, couponMonthRepository,
                pointHistoryRepository, rabbitTemplate, objectMapper);
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

        file = FileDummy.dummy(null, null, couponTemplate, product, null, null);
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        orderStateCode = OrderStateCodeDummy.dummy();
        bookpubOrder = OrderDummy.dummy(member, pricePolicy, packagePolicy, orderStateCode);

        coupon = CouponDummy.dummy(couponTemplate, bookpubOrder, orderProduct, member);

        captor = ArgumentCaptor.forClass(Coupon.class);

        orderCouponResponseDto = new GetOrderCouponResponseDto(
                1L, "testName", 1L, 1, true, 1000L, 1000L, 1000L, true
        );

        pointHistory = PointHistoryDummy.dummy(member);
    }

    @Test
    @DisplayName("쿠폰 생성 성공 테스트")
    void createCoupon_Success_Test() {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberId", "idid");

        // when
        when(memberRepository.findByMemberId(anyString()))
                .thenReturn(Optional.of(member));
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));

        // then
        couponService.createCoupon(request);

        verify(memberRepository, times(1))
                .findByMemberId(anyString());
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
        ReflectionTestUtils.setField(request, "memberId", "idid");

        // when
        when(memberRepository.findByMemberId(anyString()))
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
        ReflectionTestUtils.setField(request, "memberId", "idId");

        // when
        when(memberRepository.findByMemberId(anyString()))
                .thenReturn(Optional.of(member));
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.createCoupon(request))
                .isInstanceOf(CouponTemplateNotFoundException.class)
                .hasMessageContaining(CouponTemplateNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 수정 성공 테스트(사용 상태로 변경)")
    void modifyCoupon_Success_ChangeTime_Test() {
        // when
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));

        // then
        couponService.modifyCouponUsed(1L);

        verify(couponRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("쿠폰 수정 성공 테스트 (사용 안함 상태로 변경)")
    void modifyCoupon_Success_Test() {
        // given
        Coupon couponUsed = new Coupon(null, couponTemplate, null, null, member, true, LocalDateTime.now());

        // when
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponUsed));

        // then
        couponService.modifyCouponUsed(1L);

        verify(couponRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("쿠폰 수정 실패 테스트 (coupon 못 찾은 경우)")
    void modifyCoupon_Fail_NotFoundCoupon_Test() {
        // when
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.modifyCouponUsed(1L))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining(CouponNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 조회 성공 테스트")
    void getCoupon_Success_Test() {
        // given
        GetCouponResponseDto response =
                new GetCouponResponseDto(1L, "member", "template", "image", "일반", true, 1L, 10L, 100L, LocalDateTime.now(), true);

        // when
        when(couponRepository.findByCouponNo(anyLong()))
                .thenReturn(Optional.of(response));

        // then
        couponService.getCoupon(anyLong());

        verify(couponRepository, times(1))
                .findByCouponNo(anyLong());
    }

    @Test
    @DisplayName("쿠폰 조회 실패 테스트 (coupon 못 찾은 경우)")
    void getCoupon_Fail_NotFoundCoupon_Test() {
        // when
        when(couponRepository.findByCouponNo(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponService.getCoupon(1L))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining(CouponNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 전체 조회 테스트")
    void getCoupons_Success_Test() throws IOException {
        // given
        GetCouponResponseDto response =
                new GetCouponResponseDto(1L, "member", "template", "image", "일반", true, 1L, 10L, 100L, LocalDateTime.now(), true);
        Pageable pageable = Pageable.ofSize(10);
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(response), pageable, 1);

        // when
        when(couponRepository.findAllBy(pageable, "", ""))
                .thenReturn(page);

        // then
        couponService.getCoupons(pageable, "", "");

        verify(couponRepository, times(1))
                .findAllBy(pageable, "", "");
    }

    @Test
    @DisplayName("주문에 사용될 쿠폰 조회 테스트")
    void getOrderCouponsTest_Success() {
        // given
        // when
        when(memberRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(couponRepository.findByProductNo(anyLong(), anyLong())).thenReturn(List.of(orderCouponResponseDto));

        // then
        couponService.getOrderCoupons(1L, 1L);

        verify(memberRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).existsById(anyLong());
        verify(couponRepository, times(1)).findByProductNo(anyLong(), anyLong());
    }

    @Test
    @DisplayName("주문에 사용될 쿠폰 조회 실패 테스트_없는 멤버의 쿠폰을 찾을 경우")
    void getOrderCouponsTest_Fail_NotFoundMember() {
        // when
        when(memberRepository.existsById(anyLong())).thenReturn(false);
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(couponRepository.findByProductNo(anyLong(), anyLong())).thenReturn(List.of(orderCouponResponseDto));

        // then
        assertThatThrownBy(() -> couponService.getOrderCoupons(1L, 1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("주문에 사용될 쿠폰 조회 실패 테스트_없는 상품을 찾을 경우")
    void getOrderCouponsTest_Fail_NotFoundProduct() {

        // when
        when(memberRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(false);
        when(couponRepository.findByProductNo(anyLong(), anyLong())).thenReturn(List.of(orderCouponResponseDto));

        // then
        assertThatThrownBy(() -> couponService.getOrderCoupons(1L, 1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("사용가능한 쿠폰리스트를 반환 exception 회원을 찾을수없을 경우")
    void getPositiveCouponList() {
        // when
        when(memberRepository.existsById(anyLong()))
                .thenReturn(false);
        PageRequest pageRequest = PageRequest.of(0, 10);

        assertThatThrownBy(() -> couponService.getPositiveCouponList(pageRequest, 1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("사용가능한 쿠폰리스트를 반환 성공")
    void getPositiveCouponListSuccess() {
        // when
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(getCouponResponseDto));

        when(memberRepository.existsById(anyLong()))
                .thenReturn(true);
        when(couponRepository.findPositiveCouponByMemberNo(any(), anyLong()))
                .thenReturn(page);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<GetCouponResponseDto> pageResponse = couponService.getPositiveCouponList(pageRequest, 1L);
        List<GetCouponResponseDto> result = pageResponse.getContent();

        assertThat(pageResponse).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCouponNo()).isEqualTo(getCouponResponseDto.getCouponNo());
        assertThat(result.get(0).getMemberId()).isEqualTo(getCouponResponseDto.getMemberId());
        assertThat(result.get(0).getTemplateName()).isEqualTo(getCouponResponseDto.getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(getCouponResponseDto.getTemplateImage());
        assertThat(result.get(0).getTypeName()).isEqualTo(getCouponResponseDto.getTypeName());
        assertThat(result.get(0).isPolicyFixed()).isEqualTo(getCouponResponseDto.isPolicyFixed());
        assertThat(result.get(0).getPolicyPrice()).isEqualTo(getCouponResponseDto.getPolicyPrice());
        assertThat(result.get(0).getPolicyMinimum()).isEqualTo(getCouponResponseDto.getPolicyMinimum());
        assertThat(result.get(0).getMaxDiscount()).isEqualTo(getCouponResponseDto.getMaxDiscount());
        assertThat(result.get(0).getFinishedAt()).isEqualTo(getCouponResponseDto.getFinishedAt());
        assertThat(result.get(0).isCouponUsed()).isEqualTo(getCouponResponseDto.isCouponUsed());

        verify(memberRepository, times(1))
                .existsById(1L);
        verify(couponRepository, times(1))
                .findPositiveCouponByMemberNo(pageRequest, 1L);
    }

    @Test
    @DisplayName("사용 불가능한 쿠폰을 조회할 경우 : 회원을 찾지못함")
    void getNegativeCouponListFail() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(memberRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThatThrownBy(() -> couponService.getNegativeCouponList(pageRequest, 1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);

    }

    @Test
    @DisplayName("사용 불가능한 쿠폰을 조회한 경우")
    void getNegativeCouponList() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<GetCouponResponseDto> pageResponse = new PageImpl<>(List.of(getCouponResponseDto));
        when(memberRepository.existsById(anyLong()))
                .thenReturn(true);
        when(couponRepository.findNegativeCouponByMemberNo(any(), anyLong()))
                .thenReturn(pageResponse);

        Page<GetCouponResponseDto> page = couponService.getNegativeCouponList(pageRequest, 1L);
        List<GetCouponResponseDto> result = page.getContent();

        assertThat(page).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCouponNo()).isEqualTo(getCouponResponseDto.getCouponNo());
        assertThat(result.get(0).getMemberId()).isEqualTo(getCouponResponseDto.getMemberId());
        assertThat(result.get(0).getTemplateName()).isEqualTo(getCouponResponseDto.getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(getCouponResponseDto.getTemplateImage());
        assertThat(result.get(0).getTypeName()).isEqualTo(getCouponResponseDto.getTypeName());
        assertThat(result.get(0).isPolicyFixed()).isEqualTo(getCouponResponseDto.isPolicyFixed());
        assertThat(result.get(0).getPolicyPrice()).isEqualTo(getCouponResponseDto.getPolicyPrice());
        assertThat(result.get(0).getPolicyMinimum()).isEqualTo(getCouponResponseDto.getPolicyMinimum());
        assertThat(result.get(0).getMaxDiscount()).isEqualTo(getCouponResponseDto.getMaxDiscount());
        assertThat(result.get(0).getFinishedAt()).isEqualTo(getCouponResponseDto.getFinishedAt());
        assertThat(result.get(0).isCouponUsed()).isEqualTo(getCouponResponseDto.isCouponUsed());

        verify(memberRepository, times(1))
                .existsById(1L);
        verify(couponRepository, times(1))
                .findNegativeCouponByMemberNo(pageRequest, 1L);
    }

    @DisplayName("포인트 쿠폰 사용여부 수정 : 쿠폰을 찾을수 없을경우")
    @Test
    void modifyPointCouponUsedFail1() {
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> couponService.modifyPointCouponUsed(1L, 1L))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining(CouponNotFoundException.MESSAGE);
    }

    @DisplayName("포인트 쿠폰 사용여부 수정 : 회원을 찾을수 없을경우")
    @Test
    void modifyPointCouponUsedFail2() {
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.modifyPointCouponUsed(1L, 1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("포인트 쿠폰 사용여부 수정")
    @Test
    void modifyPointCouponUsedSuccess() {
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pointHistoryRepository.save(any()))
                .thenReturn(pointHistory);

        couponService.modifyPointCouponUsed(1L, 1L);

        verify(couponRepository, times(1))
                .findById(1L);
        verify(memberRepository, times(1))
                .findById(1L);
        verify(pointHistoryRepository, times(1))
                .save(any());
    }

    @DisplayName("이달의 쿠폰 발행 여부 확인")
    @Test
    void existsCouponMonthIssuedTest() {
        when(couponRepository.existsMonthCoupon(anyLong(), anyLong()))
                .thenReturn(true);

        boolean result = couponService.existsCouponMonthIssued(1L, 1L);

        assertThat(result).isTrue();
        verify(couponRepository, times(1))
                .existsMonthCoupon(1L, 1L);
    }

    @DisplayName("회원의 이달의 쿠폰 리스트 발행 여부를 확인 성공")
    @Test
    void existsCouponMonthListIssued() {
        when(couponRepository.existsMonthCouponList(anyLong(), any()))
                .thenReturn(List.of(1L, 2L, 3L));

        List<Boolean> result = couponService.existsCouponMonthListIssued(1L, List.of(1L, 2L, 3L));

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isTrue();
        assertThat(result.get(1)).isTrue();
        assertThat(result.get(2)).isTrue();

        verify(couponRepository, times(1))
                .existsMonthCouponList(1L, List.of(1L, 2L, 3L));
    }

    @DisplayName("멤버에게 등급 쿠폰을 발급 : 쿠폰 테플릿이 없을경우")
    @Test
    void issueTierCouponsByMemberNoFail1() {
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.issueTierCouponsByMemberNo(1L, List.of(1L)))
                .isInstanceOf(CouponTemplateNotFoundException.class)
                .hasMessageContaining(CouponTemplateNotFoundException.MESSAGE);
    }

    @DisplayName("멤버에게 등급 쿠폰을 발급 : 회원이 없는경우")
    @Test
    void issueTierCouponsByMemberNoFail2() {
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.issueTierCouponsByMemberNo(1L, List.of(1L)))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("멤버에게 등급 쿠폰을 발급")
    @Test
    void issueTierCouponsByMemberNo() {
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(couponRepository.save(any()))
                .thenReturn(coupon);

        couponService.issueTierCouponsByMemberNo(1L, List.of(1L));

        verify(couponTemplateRepository, times(1))
                .findById(1L);
        verify(memberRepository, times(1))
                .findById(1L);
        verify(couponRepository, times(1))
                .save(any());
    }

    @DisplayName("멤버의 등급쿠폰 발급 유무를 확인")
    @Test
    void existsCouponsByMemberNo(){
        when(couponRepository.existsTierCouponsByMemberNo(any(), any()))
                .thenReturn(true);

        boolean result = couponService.existsCouponsByMemberNo(1L, List.of(1L));

        assertThat(result).isTrue();
        verify(couponRepository, times(1))
                .existsTierCouponsByMemberNo(1L, List.of(1L));
    }

}