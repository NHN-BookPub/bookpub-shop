package com.nhnacademy.bookpubshop.couponstatecode.repository;

import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.couponstatecode.entity.QCouponStateCode;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 쿠폰상태코드 Repo 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponStateCodeRepositoryImpl extends QuerydslRepositorySupport
        implements CouponStateCodeRepositoryCustom {

    public CouponStateCodeRepositoryImpl() {
        super(CouponStateCode.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponStateCodeResponseDto> findByCodeNoAndCodeUsedTrue(Integer codeNo) {
        QCouponStateCode couponStateCode = QCouponStateCode.couponStateCode;

        return Optional.of(from(couponStateCode)
                .where(couponStateCode.codeNo.eq(codeNo)
                        .and(couponStateCode.codeUsed.isTrue()))
                .select(Projections.constructor(GetCouponStateCodeResponseDto.class,
                        couponStateCode.codeNo,
                        couponStateCode.codeTarget))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponStateCodeResponseDto> findAllByCodeUsedTrue() {
        QCouponStateCode couponStateCode = QCouponStateCode.couponStateCode;

        return from(couponStateCode)
                .where(couponStateCode.codeUsed.eq(true)
                        .and(couponStateCode.codeUsed.isTrue()))
                .select(Projections.constructor(GetCouponStateCodeResponseDto.class,
                        couponStateCode.codeNo,
                        couponStateCode.codeTarget
                ))
                .fetch();
    }
}
