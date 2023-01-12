package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodesException;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductTypeStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
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
 * ProductTypeStateCodeService 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class ProductTypeStateCodeServiceTest {
    ProductTypeStateCodeRepository productTypeStateCodeRepository;
    ProductTypeStateCodeService productTypeStateCodeService;
    ProductTypeStateCode productTypeStateCode;
    CreateProductTypeStateCodeRequestDto requestDto;
    GetProductTypeStateCodeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        productTypeStateCodeRepository = Mockito.mock(ProductTypeStateCodeRepository.class);
        productTypeStateCodeService =
                new ProductTypeStateCodeServiceImpl(productTypeStateCodeRepository);

        productTypeStateCode = new ProductTypeStateCode(1, "test", true, "test");

        requestDto = new CreateProductTypeStateCodeRequestDto();
        ReflectionTestUtils.setField(requestDto, "codeName", "test");
        ReflectionTestUtils.setField(requestDto, "codeUsed", true);
        ReflectionTestUtils.setField(requestDto, "codeInfo", "test");

        responseDto = new GetProductTypeStateCodeResponseDto(1, "test", true, "test");
    }

    @Test
    @DisplayName("상품 유형 상태 코드 생성 성공")
    void createTypeStateCode() {
        when(productTypeStateCodeRepository.save(any()))
                .thenReturn(productTypeStateCode);

        GetProductTypeStateCodeResponseDto result = productTypeStateCodeService.createTypeStateCode(requestDto);

        assertThat(result.getCodeNo()).isEqualTo(productTypeStateCode.getCodeNo());
        assertThat(result.getCodeName()).isEqualTo(productTypeStateCode.getCodeName());
        assertThat(result.getCodeInfo()).isEqualTo(productTypeStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productTypeStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품 유형 상태 코드 번호로 조회 성공")
    void getTypeStateCodeById() {
        when(productTypeStateCodeRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productTypeStateCode));

        GetProductTypeStateCodeResponseDto result =
                productTypeStateCodeService.getTypeStateCodeById(productTypeStateCode.getCodeNo());

        assertThat(result.getCodeNo()).isEqualTo(productTypeStateCode.getCodeNo());
        assertThat(result.getCodeName()).isEqualTo(productTypeStateCode.getCodeName());
        assertThat(result.getCodeInfo()).isEqualTo(productTypeStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productTypeStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품 유형 상태 코드 번호로 조회 실패")
    void getTypeStateCodeByIdFailed() {
        when(productTypeStateCodeRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productTypeStateCodeService.getTypeStateCodeById(productTypeStateCode.getCodeNo()))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("상품 유형 상태 코드 전체 조회 성공")
    void getAllTypeStateCodes() {
        List<ProductTypeStateCode> returns = new ArrayList<>();
        returns.add(productTypeStateCode);

        when(productTypeStateCodeRepository.findAll())
                .thenReturn(returns);

        assertThat(productTypeStateCodeService.getAllTypeStateCodes().get(0).getCodeNo())
                .isEqualTo(responseDto.getCodeNo());
        assertThat(productTypeStateCodeService.getAllTypeStateCodes().get(0).getCodeInfo())
                .isEqualTo(responseDto.getCodeInfo());
        assertThat(productTypeStateCodeService.getAllTypeStateCodes().get(0).getCodeName())
                .isEqualTo(responseDto.getCodeName());
        assertThat(productTypeStateCodeService.getAllTypeStateCodes().get(0).isCodeUsed())
                .isEqualTo(responseDto.isCodeUsed());
    }

    @Test
    @DisplayName("상품 유형 상태 코드 전체 조회 실패")
    void getAllTypeStateCodesFailed() {
        when(productTypeStateCodeRepository.findAll())
                .thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() ->productTypeStateCodeService.getAllTypeStateCodes())
                .isInstanceOf(NotFoundStateCodesException.class);
    }

    @Test
    @DisplayName("상품유형상태코드 사용여부 설정 성공")
    void setUsedTypeCodeById() {
        when(productTypeStateCodeRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productTypeStateCode));
        when(productTypeStateCodeRepository.save(any()))
                .thenReturn(productTypeStateCode);

        GetProductTypeStateCodeResponseDto result =
                productTypeStateCodeService
                        .setUsedTypeCodeById(productTypeStateCode.getCodeNo(),
                                productTypeStateCode.isCodeUsed());

        assertThat(result.getCodeNo()).isEqualTo(productTypeStateCode.getCodeNo());
        assertThat(result.getCodeName()).isEqualTo(productTypeStateCode.getCodeName());
        assertThat(result.getCodeInfo()).isEqualTo(productTypeStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productTypeStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품유형상태코드 사용여부 설정 실패")
    void setUsedTypeCodeByIdFailed() {
        when(productTypeStateCodeRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->productTypeStateCodeService.getAllTypeStateCodes())
                .isInstanceOf(NotFoundStateCodesException.class);
    }
}