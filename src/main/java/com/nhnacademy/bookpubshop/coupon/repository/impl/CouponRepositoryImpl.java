package com.nhnacademy.bookpubshop.coupon.repository.impl;

import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.entity.QCoupon;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.bookpubshop.couponpolicy.entity.QCouponPolicy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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

        QCoupon coupon = QCoupon.coupon;
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
        QMember member = QMember.member;
        QFile file = QFile.file;

        return Optional.of(from(coupon)
                        .where(coupon.couponNo.eq(couponNo))
                        .leftJoin(coupon.couponTemplate, file.couponTemplate)
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(couponTemplate.couponPolicy, couponPolicy)
                .innerJoin(coupon.member, member)
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.nameSaved.concat(file.fileExtension),
                        couponPolicy.policyFixed,
                        couponPolicy.discountRate,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> getCoupons(Pageable pageable) {
        QCoupon coupon = QCoupon.coupon;
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
        QMember member = QMember.member;
        QFile file = QFile.file;

        JPQLQuery<Long> count = from(coupon)
                .select(coupon.count());

        List<GetCouponResponseDto> content = from(coupon)
                .leftJoin(coupon.couponTemplate, file.couponTemplate)
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(couponTemplate.couponPolicy, couponPolicy)
                .innerJoin(coupon.member, member)
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.nameSaved.concat(file.fileExtension),
                        couponPolicy.policyFixed,
                        couponPolicy.discountRate,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
