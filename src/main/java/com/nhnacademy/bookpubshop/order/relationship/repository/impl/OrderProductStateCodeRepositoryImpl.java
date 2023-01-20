package com.nhnacademy.bookpubshop.order.relationship.repository.impl;

import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.custom.OrderProductStateCodeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * QueryDsl 사용을 위한 Custom 클래스의 구현체.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class OrderProductStateCodeRepositoryImpl extends QuerydslRepositorySupport
        implements OrderProductStateCodeRepositoryCustom {
    QOrderProductStateCode orderProductStateCode = QOrderProductStateCode.orderProductStateCode;
    public OrderProductStateCodeRepositoryImpl() {
        super(OrderProductStateCode.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetOrderProductStateCodeResponseDto> findCodeById(Integer id) {
        return Optional.of(
                from(orderProductStateCode)
                .select(Projections.constructor(
                        GetOrderProductStateCodeResponseDto.class,
                        orderProductStateCode.codeNo,
                        orderProductStateCode.codeName,
                        orderProductStateCode.codeUsed,
                        orderProductStateCode.codeInfo))
                .where(orderProductStateCode.codeNo.eq(id))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetOrderProductStateCodeResponseDto> findCodeAll() {
        return from(orderProductStateCode)
                .select(Projections.constructor(
                        GetOrderProductStateCodeResponseDto.class,
                        orderProductStateCode.codeNo,
                        orderProductStateCode.codeName,
                        orderProductStateCode.codeUsed,
                        orderProductStateCode.codeInfo))
                .fetch();
    }
}
