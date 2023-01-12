package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodesException;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductSaleStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;


/**
 * ProductSaleSateCodeService 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class ProductSaleStateCodeServiceTest {
    ProductSaleStateCodeRepository productSaleStateCodeRepository;
    ProductSaleStateCodeService productSaleStateCodeService;
    ProductSaleStateCode productSaleStateCode;
    CreateProductSaleStateCodeRequestDto requestDto;
    GetProductSaleStateCodeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        productSaleStateCodeRepository = Mockito.mock(ProductSaleStateCodeRepository.class);
        productSaleStateCodeService = new ProductSaleStateCodeServiceImpl(productSaleStateCodeRepository);
        productSaleStateCode = new ProductSaleStateCode(
                1,
                "test",
                true,
                "test");
        requestDto = new CreateProductSaleStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto, "codeCategory", "test");
        ReflectionTestUtils.setField(requestDto, "codeUsed", true);
        ReflectionTestUtils.setField(requestDto, "codeInfo", "test");

        responseDto = new GetProductSaleStateCodeResponseDto(
                productSaleStateCode.getCodeNumber(),
                productSaleStateCode.getCodeCategory(),
                productSaleStateCode.isCodeUsed(),
                productSaleStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("판매유형코드 생성 성공")
    void createSaleCode() {
        when(productSaleStateCodeRepository.save(any())).thenReturn(productSaleStateCode);

        GetProductSaleStateCodeResponseDto result = productSaleStateCodeService.createSaleCode(requestDto);

        assertThat(result.getCodeNumber()).isEqualTo(productSaleStateCode.getCodeNumber());
        assertThat(result.getCodeCategory()).isEqualTo(productSaleStateCode.getCodeCategory());
        assertThat(result.getCodeInfo()).isEqualTo(productSaleStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productSaleStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품유형코드번호로 조회 성공")
    void getSaleCodeById() {
        when(productSaleStateCodeRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productSaleStateCode));

        GetProductSaleStateCodeResponseDto result =
                productSaleStateCodeService.getSaleCodeById(productSaleStateCode.getCodeNumber());

        assertThat(result.getCodeNumber()).isEqualTo(productSaleStateCode.getCodeNumber());
        assertThat(result.getCodeCategory()).isEqualTo(productSaleStateCode.getCodeCategory());
        assertThat(result.getCodeInfo()).isEqualTo(productSaleStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productSaleStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품유형코드번호로 조회 실패, 존재하지 않은 코드 검색")
    void getSaleCodeByIdFailed() {
        when(productSaleStateCodeRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productSaleStateCodeService.getSaleCodeById(productSaleStateCode.getCodeNumber()))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("상품유형코드번호로 사용여부 수정 성공")
    void setUsedSaleCodeById() {
        when(productSaleStateCodeRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productSaleStateCode));
        when(productSaleStateCodeRepository.save(any()))
                .thenReturn(productSaleStateCode);

        GetProductSaleStateCodeResponseDto result =
                productSaleStateCodeService
                        .setUsedSaleCodeById(productSaleStateCode.getCodeNumber(),
                                productSaleStateCode.isCodeUsed());

        assertThat(result.getCodeNumber()).isEqualTo(productSaleStateCode.getCodeNumber());
        assertThat(result.getCodeCategory()).isEqualTo(productSaleStateCode.getCodeCategory());
        assertThat(result.getCodeInfo()).isEqualTo(productSaleStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productSaleStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품유형코드번호로 사용여부 수정 실패")
    void setUsedSaleCodeByIdFailed() {
        when(productSaleStateCodeRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productSaleStateCodeService
                .setUsedSaleCodeById(productSaleStateCode.getCodeNumber(),
                        productSaleStateCode.isCodeUsed()))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("모든 상품유형코드 조회 성공")
    void getAllProductSaleStateCode() {
        List<ProductSaleStateCode> returns = new ArrayList<>();
        returns.add(productSaleStateCode);

        when(productSaleStateCodeRepository.findAll())
                .thenReturn(returns);

        assertThat(productSaleStateCodeService
                .getAllProductSaleStateCode().get(0).getCodeNumber())
                .isEqualTo(responseDto.getCodeNumber());
        assertThat(productSaleStateCodeService
                .getAllProductSaleStateCode().get(0).getCodeInfo())
                .isEqualTo(responseDto.getCodeInfo());
        assertThat(productSaleStateCodeService
                .getAllProductSaleStateCode().get(0).getCodeCategory())
                .isEqualTo(responseDto.getCodeCategory());
        assertThat(productSaleStateCodeService
                .getAllProductSaleStateCode().get(0).isCodeUsed())
                .isEqualTo(responseDto.isCodeUsed());
    }

    @Test
    @DisplayName("모든 상품유형코드 조회 실패, 어떤 코드도 없음")
    void getAllProductSaleStateCodeFailed() {
        when(productSaleStateCodeRepository.findAll())
                .thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> productSaleStateCodeService.getAllProductSaleStateCode())
                .isInstanceOf(NotFoundStateCodesException.class);
    }
}