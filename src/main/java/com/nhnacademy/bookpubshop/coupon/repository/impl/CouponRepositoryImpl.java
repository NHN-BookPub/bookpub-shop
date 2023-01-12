package com.nhnacademy.bookpubshop.coupon.repository.impl;

import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.entity.QCoupon;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.bookpubshop.couponpolicy.entity.QCouponPolicy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * CouponRepositoryCustom 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponRepositoryImpl extends QuerydslRepositorySupport
        implements CouponRepositoryCustom {
    public CouponRepositoryImpl() {
        super(Coupon.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponResponseDto> getCoupon(Long couponNo) {

        return Optional.of(getDetailQuery()
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> getCoupons(Pageable pageable) {
        QCoupon coupon = QCoupon.coupon;

        Long count = from(coupon)
                .select(coupon.count())
                .fetchOne();

        List<GetCouponResponseDto> content = getDetailQuery()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, count);
    }

    /**
     * 쿠폰 조회를 위한 쿼리문 메소드입니다.
     *
     * @return 조회를 위한 쿼리문
     */
    private JPQLQuery<GetCouponResponseDto> getDetailQuery() {
        QCoupon coupon = QCoupon.coupon;
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
        QMember member = QMember.member;

        return from(coupon)
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(couponTemplate.couponPolicy, couponPolicy)
                .innerJoin(coupon.member, member)
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        couponTemplate.templateImage,
                        couponPolicy.policyFixed,
                        couponPolicy.discountRate,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed));
    }
}
