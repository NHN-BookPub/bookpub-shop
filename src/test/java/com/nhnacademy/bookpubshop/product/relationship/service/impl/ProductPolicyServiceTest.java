package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductPolicyService;
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
 * ProductPolicyService 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(ProductPolicyServiceImpl.class)
class ProductPolicyServiceTest {
    @Autowired
    ProductPolicyService productPolicyService;

    @MockBean
    ProductPolicyRepository productPolicyRepository;

    ProductPolicy productPolicy;
    CreateModifyProductPolicyRequestDto requestDto;
    GetProductPolicyResponseDto responseDto;
    ArgumentCaptor<ProductPolicy> captor;

    @BeforeEach
    void setUp() {
        captor = ArgumentCaptor.forClass(ProductPolicy.class);

        productPolicy = new ProductPolicy(1, "test", true, 10);

        requestDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(requestDto, "policyMethod", productPolicy.getPolicyMethod());
        ReflectionTestUtils.setField(requestDto, "policySaved", productPolicy.isPolicySaved());
        ReflectionTestUtils.setField(requestDto, "saveRate", productPolicy.getSaveRate());

        responseDto = new GetProductPolicyResponseDto(
                productPolicy.getPolicyNo(),
                productPolicy.getPolicyMethod(),
                productPolicy.isPolicySaved(),
                productPolicy.getSaveRate());
    }

    @Test
    @DisplayName("상품정책 등록 성공")
    void createProductPolicy() {
        // given

        // when

        // then
        productPolicyService.createProductPolicy(requestDto);

        verify(productPolicyRepository, times(1))
                .save(captor.capture());
    }

    @Test
    @DisplayName("싱품정책 번호로 조회 성공")
    void getProductPolicyById() {
        // given

        // when
        when(productPolicyRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productPolicy));

        // then
        GetProductPolicyResponseDto result =
                productPolicyService.getProductPolicyById(productPolicy.getPolicyNo());

        assertThat(result.getPolicyNo()).isEqualTo(productPolicy.getPolicyNo());
        assertThat(result.getPolicyMethod()).isEqualTo(productPolicy.getPolicyMethod());
        assertThat(result.getSaveRate()).isEqualTo(productPolicy.getSaveRate());
        assertThat(result.isPolicySaved()).isEqualTo(productPolicy.isPolicySaved());

        verify(productPolicyRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("상품정책 번호로 조회 실패")
    void getProductPolicyByIdFailed() {
        when(productPolicyRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productPolicyService.getProductPolicyById(productPolicy.getPolicyNo()))
                .isInstanceOf(NotFoundProductPolicyException.class);
    }

    @Test
    @DisplayName("상품정책 수정 성공")
    void modifyProductPolicyById() {
        // given
        CreateModifyProductPolicyRequestDto modifyDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(modifyDto, "policyMethod", "변경된 정책");
        ReflectionTestUtils.setField(modifyDto, "policySaved", true);
        ReflectionTestUtils.setField(modifyDto, "saveRate", 30);

        // when
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(productPolicy));

        // then
        productPolicyService.modifyProductPolicyById(anyInt(), modifyDto);

        verify(productPolicyRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품정책 수정 실패")
    void modifyProductPolicyByIdFailed() {
        // given

        // when
        when(productPolicyRepository.findById(any()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productPolicyService
                .modifyProductPolicyById(
                        productPolicy.getPolicyNo(),
                        requestDto))
                .isInstanceOf(NotFoundProductPolicyException.class);
    }

    @Test
    @DisplayName("전체 상품정책 조회 성공")
    void getProductPolicies() {
        // given
        GetProductPolicyResponseDto dto = new GetProductPolicyResponseDto(1, "method", false, 10);
        List<GetProductPolicyResponseDto> returns = List.of(dto);

        // when
        when(productPolicyRepository.findAllPolicies())
                .thenReturn(returns);

        // then
        productPolicyService.getProductPolicies();

        verify(productPolicyRepository, times(1))
                .findAllPolicies();
    }

}