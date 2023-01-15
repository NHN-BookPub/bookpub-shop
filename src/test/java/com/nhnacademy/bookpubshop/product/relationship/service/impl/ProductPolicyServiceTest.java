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
 * ProductPolicyService 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class ProductPolicyServiceTest {
    ProductPolicyRepository productPolicyRepository;
    ProductPolicyService productPolicyService;
    ProductPolicy productPolicy;
    CreateModifyProductPolicyRequestDto requestDto;
    GetProductPolicyResponseDto responseDto;

    @BeforeEach
    void setUp() {
        productPolicyRepository = Mockito.mock(ProductPolicyRepository.class);
        productPolicyService = new ProductPolicyServiceImpl(productPolicyRepository);
        productPolicy = new ProductPolicy(1, "test", true, 10);

        requestDto = new CreateModifyProductPolicyRequestDto();
        ReflectionTestUtils.setField(requestDto,
                "policyMethod",
                productPolicy.getPolicyMethod());
        ReflectionTestUtils.setField(requestDto,
                "policySaved",
                productPolicy.isPolicySaved());
        ReflectionTestUtils.setField(requestDto,
                "saveRate",
                productPolicy.getSaveRate());

        responseDto = new GetProductPolicyResponseDto(
                productPolicy.getPolicyNo(),
                productPolicy.getPolicyMethod(),
                productPolicy.isPolicySaved(),
                productPolicy.getSaveRate());
    }

    @Test
    @DisplayName("상품정책 등록 성공")
    void createProductPolicy() {
        when(productPolicyRepository.save(any())).thenReturn(productPolicy);

        GetProductPolicyResponseDto result = productPolicyService.createProductPolicy(requestDto);

        assertThat(result.getPolicyNo()).isEqualTo(productPolicy.getPolicyNo());
        assertThat(result.getPolicyMethod()).isEqualTo(productPolicy.getPolicyMethod());
        assertThat(result.isPolicySaved()).isEqualTo(productPolicy.isPolicySaved());
        assertThat(result.getSaveRate()).isEqualTo(productPolicy.getSaveRate());
    }

    @Test
    @DisplayName("싱품정책 번호로 조회 성공")
    void getProductPolicyById() {
        when(productPolicyRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productPolicy));

        GetProductPolicyResponseDto result =
                productPolicyService.getProductPolicyById(productPolicy.getPolicyNo());

        assertThat(result.getPolicyNo()).isEqualTo(productPolicy.getPolicyNo());
        assertThat(result.getPolicyMethod()).isEqualTo(productPolicy.getPolicyMethod());
        assertThat(result.getSaveRate()).isEqualTo(productPolicy.getSaveRate());
        assertThat(result.isPolicySaved()).isEqualTo(productPolicy.isPolicySaved());
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
        when(productPolicyRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(productPolicyRepository.save(any()))
                .thenReturn(productPolicy);

        GetProductPolicyResponseDto result =
                productPolicyService.modifyProductPolicyById(
                        productPolicy.getPolicyNo(), requestDto);

        assertThat(result.getPolicyNo()).isEqualTo(productPolicy.getPolicyNo());
        assertThat(result.getPolicyMethod()).isEqualTo(productPolicy.getPolicyMethod());
        assertThat(result.getSaveRate()).isEqualTo(productPolicy.getSaveRate());
        assertThat(result.isPolicySaved()).isEqualTo(productPolicy.isPolicySaved());
    }

    @Test
    @DisplayName("상품정책 수정 실패")
    void modifyProductPolicyByIdFailed() {
        when(productPolicyRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(productPolicyRepository.save(any()))
                .thenReturn(productPolicy);

        assertThatThrownBy(() -> productPolicyService
                .modifyProductPolicyById(
                        productPolicy.getPolicyNo(),
                        requestDto))
                .isInstanceOf(NotFoundProductPolicyException.class);
    }

    @Test
    @DisplayName("전체 상품정책 조회 성공")
    void getProductPolicies() {
        List<ProductPolicy> returns = new ArrayList<>();
        returns.add(productPolicy);

        when(productPolicyRepository.findAll())
                .thenReturn(returns);

        assertThat(productPolicyService.getProductPolicies().get(0).getPolicyNo())
                .isEqualTo(responseDto.getPolicyNo());
        assertThat(productPolicyService.getProductPolicies().get(0).getSaveRate())
                .isEqualTo(responseDto.getSaveRate());
        assertThat(productPolicyService.getProductPolicies().get(0).getPolicyMethod())
                .isEqualTo(responseDto.getPolicyMethod());
        assertThat(productPolicyService.getProductPolicies().get(0).isPolicySaved())
                .isEqualTo(responseDto.isPolicySaved());
    }

    @Test
    @DisplayName("전체 상품정책 조회 실패")
    void getProductPoliciesFailed() {
        when(productPolicyRepository.findAll())
                .thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> productPolicyService.getProductPolicies())
                .isInstanceOf(NotFoundProductPolicyException.class);
    }
}