package com.nhnacademy.bookpubshop.couponmonth.repository.impl;

import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.entity.QCouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.repository.CouponMonthRepositoryCustom;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * CouponMonth 레포 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponMonthRepositoryImpl extends QuerydslRepositorySupport
        implements CouponMonthRepositoryCustom {
    public CouponMonthRepositoryImpl() {
        super(CouponMonth.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponMonthResponseDto> getCouponMonth(Long monthNo) {
        QCouponMonth couponMonth = QCouponMonth.couponMonth;
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        return Optional.of(from(couponMonth)
                .innerJoin(couponMonth.couponTemplate, couponTemplate)
                .where(couponMonth.monthNo.eq(monthNo))
                .select(Projections.constructor(GetCouponMonthResponseDto.class,
                        couponMonth.monthNo,
                        couponTemplate.templateNo,
                        couponTemplate.templateName,
                        couponTemplate.templateImage,
                        couponMonth.openedAt,
                        couponMonth.monthQuantity))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponMonthResponseDto> getCouponMonths() {
        QCouponMonth couponMonth = QCouponMonth.couponMonth;
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        return from(couponMonth)
                .innerJoin(couponMonth.couponTemplate, couponTemplate)
                .select(Projections.constructor(GetCouponMonthResponseDto.class,
                        couponMonth.monthNo,
                        couponTemplate.templateNo,
                        couponTemplate.templateName,
                        couponTemplate.templateImage,
                        couponMonth.openedAt,
                        couponMonth.monthQuantity))
                .fetch();
    }
}
