package com.nhnacademy.bookpubshop.couponmonth.repository.impl;

import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.entity.QCouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.repository.CouponMonthRepositoryCustom;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.file.entity.QFile;
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

    QCouponMonth couponMonth = QCouponMonth.couponMonth;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
    QFile file = QFile.file;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponMonthResponseDto> getCouponMonth(Long monthNo) {

        return Optional.ofNullable(from(couponMonth)
                .join(couponMonth.couponTemplate, couponTemplate)
                .leftJoin(file)
                .on(couponTemplate.templateNo.eq(file.couponTemplate.templateNo))
                .select(Projections.constructor(GetCouponMonthResponseDto.class,
                        couponMonth.monthNo,
                        couponTemplate.templateNo,
                        couponTemplate.templateName,
                        file.filePath.as("templateImage"),
                        couponMonth.openedAt,
                        couponMonth.monthQuantity))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponMonthResponseDto> getCouponMonths() {

        return from(couponMonth)
                .join(couponMonth.couponTemplate, couponTemplate)
                .leftJoin(file)
                .on(couponTemplate.templateNo.eq(file.couponTemplate.templateNo))
                .select(Projections.constructor(GetCouponMonthResponseDto.class,
                        couponMonth.monthNo,
                        couponTemplate.templateNo,
                        couponTemplate.templateName,
                        file.filePath.as("templateImage"),
                        couponMonth.openedAt,
                        couponMonth.monthQuantity))
                .fetch();
    }
}
