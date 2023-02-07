package com.nhnacademy.bookpubshop.couponmonth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.CreateCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.ModifyCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.dummy.CouponMonthDummy;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.exception.CouponMonthNotFoundException;
import com.nhnacademy.bookpubshop.couponmonth.repository.CouponMonthRepository;
import com.nhnacademy.bookpubshop.couponmonth.service.impl.CouponMonthServiceImpl;
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
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.io.IOException;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 이달의 쿠폰 서비스 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(CouponMonthServiceImpl.class)
class CouponMonthServiceTest {

    @Autowired
    CouponMonthService couponMonthService;

    @MockBean
    CouponMonthRepository couponMonthRepository;

    @MockBean
    CouponTemplateRepository couponTemplateRepository;
    @MockBean
    FileManagement fileManagement;

    ArgumentCaptor<CouponMonth> captor;

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
    CreateCouponMonthRequestDto createCouponMonthRequestDto;

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

        file = FileDummy.dummy(null, null, couponTemplate, product, null,null);
        captor = ArgumentCaptor.forClass(CouponMonth.class);

        createCouponMonthRequestDto = new CreateCouponMonthRequestDto();
    }

    @Test
    @DisplayName("이달의 쿠폰 등록 성공 테스트")
    void createCouponMonth_Success_Test() {
        // given
        ReflectionTestUtils.setField(createCouponMonthRequestDto, "templateNo", 1L);
        ReflectionTestUtils.setField(createCouponMonthRequestDto, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(createCouponMonthRequestDto, "monthQuantity", 100);

        // when
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));

        // then
        couponMonthService.createCouponMonth(createCouponMonthRequestDto);

        verify(couponTemplateRepository, times(1))
                .findById(anyLong());
        verify(couponMonthRepository, times(1))
                .save(captor.capture());
    }

    @Test
    @DisplayName("이달의 쿠폰 등록 실패 테스트(쿠폰 템플릿 번호가 없는 경우)")
    void createCouponMonth_Fail_Test() {
        // given
        ReflectionTestUtils.setField(createCouponMonthRequestDto, "templateNo", 1L);
        ReflectionTestUtils.setField(createCouponMonthRequestDto, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(createCouponMonthRequestDto, "monthQuantity", 100);

        // when
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponMonthService.createCouponMonth(createCouponMonthRequestDto))
                .isInstanceOf(CouponTemplateNotFoundException.class)
                .hasMessageContaining("은 없는 쿠폰템플릿번호입니다.");
    }

    @Test
    @DisplayName("이달의 쿠폰 수정 성공 테스트")
    void modifyCouponMonth_Success_Test() {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), 100);

        // when
        when(couponMonthRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponMonth));

        // then
        couponMonthService.modifyCouponMonth(request);

        verify(couponMonthRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("이달의 쿠폰 수정 실패 테스트(이달의 쿠폰 번호가 없는 경우")
    void modifyCouponMonth_Fail_Test() {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "monthNo", null);
        ReflectionTestUtils.setField(request, "openedAt", null);
        ReflectionTestUtils.setField(request, "monthQuantity", null);

        // when
        when(couponMonthRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponMonthService.modifyCouponMonth(request))
                .isInstanceOf(CouponMonthNotFoundException.class)
                .hasMessageContaining("없는 이달의 쿠폰번호입니다.");
    }

    @Test
    @DisplayName("이달의 쿠폰 삭제 성공 테스트")
    void deleteCouponMonth_Success_Test() {
        // when
        when(couponMonthRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponMonth));

        // then
        couponMonthService.deleteCouponMonth(anyLong());

        verify(couponMonthRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("이달의 쿠폰 삭제 실패 테스트(이달의 쿠폰 번호가 없는 경우)")
    void deleteCouponMonth_Fail_Test() {
        // when
        when(couponMonthRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponMonthService.deleteCouponMonth(1L))
                .isInstanceOf(CouponMonthNotFoundException.class)
                .hasMessageContaining("없는 이달의 쿠폰번호입니다.");
    }

    @Test
    @DisplayName("이달의 쿠폰 조회 성공 테스트")
    void getCouponMonth_Success_Test() {
        // given
        GetCouponMonthResponseDto response = new GetCouponMonthResponseDto(null, null, null, null, null, null);

        // when
        when(couponMonthRepository.getCouponMonth(anyLong()))
                .thenReturn(Optional.of(response));

        // then
        GetCouponMonthResponseDto result = couponMonthService.getCouponMonth(anyLong());

        assertThat(result.getMonthNo()).isEqualTo(response.getMonthNo());
        assertThat(result.getTemplateNo()).isEqualTo(response.getTemplateNo());
        assertThat(result.getTemplateName()).isEqualTo(response.getTemplateName());
        assertThat(result.getTemplateImage()).isEqualTo(response.getTemplateImage());
        assertThat(result.getOpenedAt()).isEqualTo(response.getOpenedAt());
        assertThat(result.getMonthQuantity()).isEqualTo(response.getMonthQuantity());

        verify(couponMonthRepository, times(1))
                .getCouponMonth(anyLong());
    }

    @Test
    @DisplayName("이달의 쿠폰 번호를 가지고 단건 조회 실패 테스트(번호가 없는 경우)")
    void getCouponMonth_Fail_Test() {
        // when
        when(couponMonthRepository.getCouponMonth(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> couponMonthService.getCouponMonth(1L))
                .isInstanceOf(CouponMonthNotFoundException.class)
                .hasMessageContaining("없는 이달의 쿠폰번호입니다.");
    }

    @Test
    @DisplayName("이달의 쿠폰 전체를 조회 성공 테스트")
    void getCouponMonths_Success_Test() throws IOException {
        // given
        GetCouponMonthResponseDto dto = new GetCouponMonthResponseDto(null, null, null, null, null, null);

        // when
        when(couponMonthRepository.getCouponMonths())
                .thenReturn(List.of(dto));

        // then
        List<GetCouponMonthResponseDto> result = couponMonthService.getCouponMonths();

        assertThat(result.get(0).getMonthNo()).isEqualTo(dto.getMonthNo());
        assertThat(result.get(0).getTemplateNo()).isEqualTo(dto.getTemplateNo());
        assertThat(result.get(0).getTemplateName()).isEqualTo(dto.getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(dto.getTemplateImage());
        assertThat(result.get(0).getOpenedAt()).isEqualTo(dto.getOpenedAt());
        assertThat(result.get(0).getMonthQuantity()).isEqualTo(dto.getMonthQuantity());

        verify(couponMonthRepository, times(1))
                .getCouponMonths();
    }

    @Test
    @DisplayName("이달의 쿠폰 전체를 조회 성공 테스트_이미지가 있는 경우")
    void getCouponMonths_Success_Test_WithImage() throws IOException {
        // given
        GetCouponMonthResponseDto dto = new GetCouponMonthResponseDto(null, null, null, "Image", null, null);

        // when
        when(couponMonthRepository.getCouponMonths())
                .thenReturn(List.of(dto));

        // then
        List<GetCouponMonthResponseDto> result = couponMonthService.getCouponMonths();

        assertThat(result.get(0).getMonthNo()).isEqualTo(dto.getMonthNo());
        assertThat(result.get(0).getTemplateNo()).isEqualTo(dto.getTemplateNo());
        assertThat(result.get(0).getTemplateName()).isEqualTo(dto.getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(dto.getTemplateImage());
        assertThat(result.get(0).getOpenedAt()).isEqualTo(dto.getOpenedAt());
        assertThat(result.get(0).getMonthQuantity()).isEqualTo(dto.getMonthQuantity());
        verify(couponMonthRepository, times(1))
                .getCouponMonths();
    }

}