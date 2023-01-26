package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * ProductSaleSateCodeService 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(ProductSaleStateCodeServiceImpl.class)
class ProductSaleStateCodeServiceTest {

    @Autowired
    ProductSaleStateCodeService productSaleStateCodeService;

    @MockBean
    ProductSaleStateCodeRepository productSaleStateCodeRepository;
    ProductSaleStateCode productSaleStateCode;
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

        responseDto = new GetProductSaleStateCodeResponseDto(
                productSaleStateCode.getCodeNo(),
                productSaleStateCode.getCodeCategory(),
                productSaleStateCode.isCodeUsed(),
                productSaleStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("상품유형코드번호로 조회 성공")
    void getSaleCodeById() {
        when(productSaleStateCodeRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productSaleStateCode));

        GetProductSaleStateCodeResponseDto result =
                productSaleStateCodeService.getSaleCodeById(productSaleStateCode.getCodeNo());

        assertThat(result.getCodeNo()).isEqualTo(productSaleStateCode.getCodeNo());
        assertThat(result.getCodeCategory()).isEqualTo(productSaleStateCode.getCodeCategory());
        assertThat(result.getCodeInfo()).isEqualTo(productSaleStateCode.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(productSaleStateCode.isCodeUsed());
    }

    @Test
    @DisplayName("상품유형코드번호로 조회 실패, 존재하지 않은 코드 검색")
    void getSaleCodeByIdFailed() {
        when(productSaleStateCodeRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productSaleStateCodeService.getSaleCodeById(productSaleStateCode.getCodeNo()))
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
                        .setUsedSaleCodeById(productSaleStateCode.getCodeNo(),
                                productSaleStateCode.isCodeUsed());

        assertThat(result.getCodeNo()).isEqualTo(productSaleStateCode.getCodeNo());
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
                .setUsedSaleCodeById(productSaleStateCode.getCodeNo(),
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
                .getAllProductSaleStateCode().get(0).getCodeNo())
                .isEqualTo(responseDto.getCodeNo());
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
    @DisplayName("사용중인 전체 판매유형코드 테스트")
    void getAllCodesByUsed() {
        // given
        List<GetProductSaleStateCodeResponseDto> list = List.of(responseDto);

        // when
        when(productSaleStateCodeRepository.findByAllUsed())
                .thenReturn(list);

        // then
        assertThat(productSaleStateCodeService.getAllProductSaleStateCodeUsed()
                .get(0).getCodeNo()).isEqualTo(list.get(0).getCodeNo());
        assertThat(productSaleStateCodeService.getAllProductSaleStateCodeUsed()
                .get(0).getCodeCategory()).isEqualTo(list.get(0).getCodeCategory());
        assertThat(productSaleStateCodeService.getAllProductSaleStateCodeUsed()
                .get(0).isCodeUsed()).isEqualTo(list.get(0).isCodeUsed());
        assertThat(productSaleStateCodeService.getAllProductSaleStateCodeUsed()
                .get(0).getCodeInfo()).isEqualTo(list.get(0).getCodeInfo());
    }
}