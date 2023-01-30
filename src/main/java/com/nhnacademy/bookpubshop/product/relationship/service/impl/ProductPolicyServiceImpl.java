package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductPolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public void createProductPolicy(
            CreateModifyProductPolicyRequestDto request) {
        productPolicyRepository.save(new ProductPolicy(
                null,
                request.getPolicyMethod(),
                request.isPolicySaved(),
                request.getSaveRate()));

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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetProductPolicyResponseDto> getProductPolicies() {
        return productPolicyRepository.findAllPolicies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductPolicyById(Integer policyNo,
                                        CreateModifyProductPolicyRequestDto policy) {
        ProductPolicy productPolicy = productPolicyRepository
                .findById(policyNo)
                .orElseThrow(NotFoundProductPolicyException::new);

        productPolicy.modifyPolicy(
                policy.getPolicyMethod(),
                policy.isPolicySaved(),
                policy.getSaveRate());
    }
}
