package com.nhnacademy.bookpubshop.coupontype.repository.impl;

import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.coupontype.entity.QCouponType;
import com.nhnacademy.bookpubshop.coupontype.repository.CouponTypeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 쿠폰유형 Repo 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTypeRepositoryImpl extends QuerydslRepositorySupport
        implements CouponTypeRepositoryCustom {

    public CouponTypeRepositoryImpl() {
        super(CouponType.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponTypeResponseDto> findByTypeNo(Long typeNo) {
        QCouponType couponType = QCouponType.couponType;

        return Optional.of(from(couponType)
                .where(couponType.typeNo.eq(typeNo))
                .select(Projections.constructor(GetCouponTypeResponseDto.class,
                        couponType.typeNo,
                        couponType.typeName))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponTypeResponseDto> findAllBy() {
        QCouponType couponType = QCouponType.couponType;

        return from(couponType)
                .select(Projections.constructor(GetCouponTypeResponseDto.class,
                        couponType.typeNo,
                        couponType.typeName))
                .fetch();
    }
}
