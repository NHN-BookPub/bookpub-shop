package com.nhnacademy.bookpubshop.pricepolicy.repository.impl;

import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.QPricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.repository.PricePolicyRepositoryCustom;
import com.nhnacademy.bookpubshop.state.PricePolicyState;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 가격정책 custom 레포지토리의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class PricePolicyRepositoryImpl extends QuerydslRepositorySupport
        implements PricePolicyRepositoryCustom {
    QPricePolicy pricePolicy = QPricePolicy.pricePolicy;

    public PricePolicyRepositoryImpl() {
        super(PricePolicy.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPricePolicyResponseDto> getPricePolicyByName(String policyName) {
        return from(pricePolicy)
                .select(Projections.constructor(
                        GetPricePolicyResponseDto.class,
                        pricePolicy.policyNo,
                        pricePolicy.policyName,
                        pricePolicy.policyFee,
                        pricePolicy.createdAt
                ))
                .where(pricePolicy.policyName.eq(policyName))
                .orderBy(pricePolicy.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<PricePolicy> getPricePolicyById(Integer policyNo) {
        return Optional.of(from(pricePolicy)
                .select(pricePolicy)
                .where(pricePolicy.policyNo.eq(policyNo))
                .orderBy(pricePolicy.createdAt.desc())
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPricePolicyResponseDto> findAllPolicies() {
        return from(pricePolicy)
                .select(Projections.constructor(
                        GetPricePolicyResponseDto.class,
                        pricePolicy.policyNo,
                        pricePolicy.policyName,
                        pricePolicy.policyFee,
                        pricePolicy.createdAt
                ))
                .orderBy(pricePolicy.createdAt.desc())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PricePolicy> getLatestPricePolicyByName(String name) {
        return Optional.of(from(pricePolicy)
                .select(pricePolicy)
                .where(pricePolicy.policyName.eq(name))
                .orderBy(pricePolicy.createdAt.desc())
                .fetchOne());
    }

    @Override
    public List<GetOrderPolicyResponseDto> getShipAndPackagePolicy() {
        GetOrderPolicyResponseDto packingPolicy = from(pricePolicy)
                .select(Projections.constructor(GetOrderPolicyResponseDto.class,
                        pricePolicy.policyNo,
                        pricePolicy.policyName,
                        pricePolicy.policyFee
                ))
                .where(pricePolicy.policyName.eq(PricePolicyState.PACKAGING.getName()))
                .orderBy(pricePolicy.createdAt.desc())
                .limit(1)
                .fetchOne();

        GetOrderPolicyResponseDto shippingPolicy = from(pricePolicy)
                .select(Projections.constructor(GetOrderPolicyResponseDto.class,
                        pricePolicy.policyNo,
                        pricePolicy.policyName,
                        pricePolicy.policyFee
                ))
                .where(pricePolicy.policyName.eq(PricePolicyState.SHIPPING.getName()))
                .orderBy(pricePolicy.createdAt.desc())
                .limit(1)
                .fetchOne();

        return List.of(packingPolicy, shippingPolicy);
    }
}
