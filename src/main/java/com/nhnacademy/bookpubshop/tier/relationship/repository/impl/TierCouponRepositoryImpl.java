package com.nhnacademy.bookpubshop.tier.relationship.repository.impl;

import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.tier.entity.QBookPubTier;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import com.nhnacademy.bookpubshop.tier.relationship.entity.QTierCoupon;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import com.nhnacademy.bookpubshop.tier.relationship.repository.TierCouponRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * TierCouponRepository 구현체.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class TierCouponRepositoryImpl extends QuerydslRepositorySupport
        implements TierCouponRepositoryCustom {

    public TierCouponRepositoryImpl() {
        super(TierCoupon.class);
    }

    QTierCoupon tierCoupon = QTierCoupon.tierCoupon;
    QBookPubTier tier = QBookPubTier.bookPubTier;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetTierCouponResponseDto> findAllBy(Pageable pageable) {

        JPQLQuery<Long> count = from(tierCoupon).select(tierCoupon.count());
        List<GetTierCouponResponseDto> content = from(tierCoupon)
                .join(tierCoupon.bookPubTier, tier)
                .join(tierCoupon.couponTemplate, couponTemplate)
                .select(Projections.constructor(GetTierCouponResponseDto.class,
                        tierCoupon.pk.couponTemplateNo,
                        couponTemplate.templateName,
                        tierCoupon.pk.tierNo,
                        tier.tierName
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> findAllByTierNo(Integer tierNo) {
        return from(tierCoupon)
                .select(tierCoupon.pk.couponTemplateNo)
                .where(tierCoupon.pk.tierNo.eq(tierNo))
                .fetch();
    }
}
