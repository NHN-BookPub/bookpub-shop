package com.nhnacademy.bookpubshop.couponpolicy.repository.impl;

import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.QCouponPolicy;
import com.nhnacademy.bookpubshop.couponpolicy.repository.CouponPolicyRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 쿠폰정책 Repo 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponPolicyRepositoryImpl extends QuerydslRepositorySupport
        implements CouponPolicyRepositoryCustom {

    public CouponPolicyRepositoryImpl() {
        super(CouponPolicy.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponPolicyResponseDto> findByPolicyNo(Integer policyNo) {
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

        return Optional.of(from(couponPolicy)
                .where(couponPolicy.policyNo.eq(policyNo))
                .select(Projections.constructor(GetCouponPolicyResponseDto.class,
                        couponPolicy.policyNo,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponPolicyResponseDto> findByAll() {
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

        return from(couponPolicy)
                .select(Projections.constructor(GetCouponPolicyResponseDto.class,
                        couponPolicy.policyNo,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount))
                .fetch();
    }
}
