package com.nhnacademy.bookpubshop.order.relationship.repository.impl;

import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepositoryCustom;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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
    QProduct product = QProduct.product;
    QOrderProductStateCode orderProductStateCode = QOrderProductStateCode.orderProductStateCode;
    QFile file = QFile.file;
    QMember member = QMember.member;

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

    @Override
    public Page<GetExchangeResponseDto> getExchangeOrderProductList(Pageable pageable) {
        List<GetExchangeResponseDto> query = from(orderProduct)
                .innerJoin(orderProduct.product, product)
                .innerJoin(orderProduct.orderProductStateCode, orderProductStateCode)
                .innerJoin(orderProduct.order, order)
                .innerJoin(orderProduct.order.member, member)
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull())))
                .select(Projections.constructor(GetExchangeResponseDto.class,
                        orderProduct.orderProductNo,
                        member.memberId,
                        product.productNo,
                        product.title,
                        file.filePath.as(FileCategory.PRODUCT_THUMBNAIL.getCategory()),
                        orderProduct.productAmount,
                        orderProduct.orderProductStateCode.codeName.as("stateCode"),
                        orderProduct.exchangeReason
                )).where(orderProduct.orderProductStateCode.codeName
                        .eq(OrderProductState.WAITING_EXCHANGE.getName()))
                .orderBy(order.createdAt.desc())
                .fetch();

        JPQLQuery<Long> count = from(orderProduct)
                .innerJoin(orderProduct.product, product)
                .innerJoin(orderProduct.orderProductStateCode, orderProductStateCode)
                .innerJoin(orderProduct.order, order)
                .innerJoin(orderProduct.order.member, member)
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull())))
                .where(orderProduct.orderProductStateCode.codeName
                        .eq(OrderProductState.WAITING_EXCHANGE.getName()))
                .select(orderProduct.count());

        return PageableExecutionUtils.getPage(query, pageable, count::fetchOne);
    }
}
