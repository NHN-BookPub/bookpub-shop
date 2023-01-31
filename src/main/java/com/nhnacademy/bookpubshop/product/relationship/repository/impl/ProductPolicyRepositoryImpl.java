package com.nhnacademy.bookpubshop.product.relationship.repository.impl;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 커스텀 상품정책 레포지토리 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class ProductPolicyRepositoryImpl extends QuerydslRepositorySupport implements ProductPolicyRepositoryCustom {

    public ProductPolicyRepositoryImpl() {
        super(ProductPolicyRepository.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductPolicyResponseDto> findAllPolicies() {
        QProductPolicy productPolicy = QProductPolicy.productPolicy;

        return from(productPolicy)
                .select(Projections.constructor(GetProductPolicyResponseDto.class,
                        productPolicy.policyNo,
                        productPolicy.policyMethod,
                        productPolicy.policySaved,
                        productPolicy.saveRate))
                .fetch();
    }
}
