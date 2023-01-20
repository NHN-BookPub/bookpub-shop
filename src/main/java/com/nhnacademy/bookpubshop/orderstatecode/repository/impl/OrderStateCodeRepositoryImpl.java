package com.nhnacademy.bookpubshop.orderstatecode.repository.impl;

import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.entity.QOrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * custom 레포지토리의 구현체.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class OrderStateCodeRepositoryImpl extends QuerydslRepositorySupport
        implements OrderStateCodeRepositoryCustom {
    QOrderStateCode orderStateCode = QOrderStateCode.orderStateCode;

    public OrderStateCodeRepositoryImpl() {
        super(OrderStateCode.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetOrderStateCodeResponseDto> findStateCodeByNo(Integer codeNo) {
        return Optional.of(from(orderStateCode)
                .select(Projections.constructor(
                        GetOrderStateCodeResponseDto.class,
                        orderStateCode.codeNo,
                        orderStateCode.codeName,
                        orderStateCode.codeUsed,
                        orderStateCode.codeInfo))
                        .where(orderStateCode.codeNo.eq(codeNo))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetOrderStateCodeResponseDto> findStateCodes() {
        return from(orderStateCode)
                .select(Projections.constructor(
                        GetOrderStateCodeResponseDto.class,
                        orderStateCode.codeNo,
                        orderStateCode.codeName,
                        orderStateCode.codeUsed,
                        orderStateCode.codeInfo
                )).fetch();
    }
}
