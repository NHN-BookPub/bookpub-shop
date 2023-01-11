package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductPolicyService;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품정책 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class ProductPolicyServiceImpl implements ProductPolicyService {
    private final ProductPolicyRepository productPolicyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductPolicyResponseDto createProductPolicy(CreateModifyProductPolicyRequestDto request) {
        ProductPolicy productPolicy = productPolicyRepository.save(
                new ProductPolicy(
                        null,
                        request.getPolicyMethod(),
                        request.isPolicySaved(),
                        request.getSaveRate()));

        return new GetProductPolicyResponseDto(
                productPolicy.getPolicyNo(),
                productPolicy.getPolicyMethod(),
                productPolicy.isPolicySaved(),
                productPolicy.getSaveRate()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GetProductPolicyResponseDto getProductPolicyById(Integer policyNo) {
        ProductPolicy productPolicy =
                productPolicyRepository
                        .findById(policyNo)
                        .orElseThrow(NotFoundProductPolicyException::new);

        return new GetProductPolicyResponseDto(
                productPolicy.getPolicyNo(),
                productPolicy.getPolicyMethod(),
                productPolicy.isPolicySaved(),
                productPolicy.getSaveRate()
        );
    }

    @Override
    public List<GetProductPolicyResponseDto> getProductPolicies() {
        List<ProductPolicy> productPolicies = productPolicyRepository.findAll();
        List<GetProductPolicyResponseDto> returns = new ArrayList<>();

        if (productPolicies.isEmpty()) {
            throw new NotFoundProductPolicyException();
        }

        for (ProductPolicy policy : productPolicies) {
            returns.add(new GetProductPolicyResponseDto(
                    policy.getPolicyNo(),
                    policy.getPolicyMethod(),
                    policy.isPolicySaved(),
                    policy.getSaveRate()
            ));
        }

        return returns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductPolicyResponseDto modifyProductPolicyById(Integer policyNo,
                                                 CreateModifyProductPolicyRequestDto policy) {
        ProductPolicy productPolicy =
                productPolicyRepository
                        .findById(policyNo)
                        .orElseThrow(NotFoundProductPolicyException::new);

        ProductPolicy savePolicy = productPolicyRepository.save(
                new ProductPolicy(
                        productPolicy.getPolicyNo(),
                        policy.getPolicyMethod(),
                        policy.isPolicySaved(),
                        policy.getSaveRate()));

        return new GetProductPolicyResponseDto(
                savePolicy.getPolicyNo(),
                savePolicy.getPolicyMethod(),
                savePolicy.isPolicySaved(),
                savePolicy.getSaveRate()
        );
    }
}
