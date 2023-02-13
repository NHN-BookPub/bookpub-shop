package com.nhnacademy.bookpubshop.order.relationship.repository.impl;

import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 주문상품 레포지토리의 구현체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class OrderProductRepositoryImpl extends QuerydslRepositorySupport
        implements OrderProductRepositoryCustom {

    QOrderProduct orderProduct = QOrderProduct.orderProduct;
    QBookpubOrder order = QBookpubOrder.bookpubOrder;

    public OrderProductRepositoryImpl() {
        super(OrderProduct.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderProduct> getOrderProductList(Long orderNo) {
        return from(orderProduct)
                .innerJoin(orderProduct.order, order)
                .select(orderProduct)
                .where(orderProduct.order.orderNo.eq(orderNo))
                .fetch();
    }

    @Override
    public Optional<OrderProduct> getOrderProduct(Long orderProductNo) {
        return Optional.of(
                from(orderProduct)
                        .select(orderProduct)
                        .where(orderProduct.orderProductNo.eq(orderProductNo))
                        .fetchOne()
        );
    }
}
