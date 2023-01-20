package com.nhnacademy.bookpubshop.order.repository.impl;

import static com.querydsl.jpa.JPAExpressions.select;
import com.nhnacademy.bookpubshop.order.dto.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.repository.OrderRepositoryCustom;
import com.nhnacademy.bookpubshop.orderstatecode.entity.QOrderStateCode;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 주문레포지토리에서 querydsl을 위한 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class OrderRepositoryImpl extends QuerydslRepositorySupport
        implements OrderRepositoryCustom {
    QBookpubOrder order = QBookpubOrder.bookpubOrder;
    QProduct product = QProduct.product;
    QOrderProduct orderProduct = QOrderProduct.orderProduct;
    QOrderStateCode orderStateCode = QOrderStateCode.orderStateCode;

    public OrderRepositoryImpl() {
        super(BookpubOrder.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetOrderDetailResponseDto> getOrderDetailById(Long orderNo) {
        return Optional.of(
                from(order)
                        .select(Projections.constructor(
                                GetOrderDetailResponseDto.class,
                                order.orderNo,
                                orderStateCode.codeName,
                                order.orderBuyer,
                                order.buyerPhone,
                                order.orderRecipient,
                                order.recipientPhone,
                                order.roadAddress,
                                order.addressDetail,
                                order.createdAt,
                                order.receivedAt,
                                order.invoiceNumber,
                                order.orderPackaged,
                                order.packagingPricePolicy.policyFee,
                                order.deliveryPricePolicy.policyFee,
                                order.orderRequest,
                                order.pointAmount,
                                order.couponDiscount,
                                order.orderPrice
                        ))
                        .join(orderProduct).on(orderProduct.order.orderNo.eq(order.orderNo))
                        .join(product).on(orderProduct.product.productNo.eq(product.productNo))
                        .join(orderStateCode).on(orderStateCode.codeNo.eq(order.orderStateCode.codeNo))
                        .where(order.orderNo.eq(orderNo))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetOrderListResponseDto> getOrdersList(Pageable pageable) {
        JPQLQuery<GetOrderListResponseDto> query =
                from(order)
                .select(Projections.constructor(
                        GetOrderListResponseDto.class,
                        order.orderNo,
                        order.orderStateCode.codeName,
                        order.createdAt,
                        order.receivedAt,
                        order.invoiceNumber,
                        order.orderPrice
                ))
                .join(orderProduct).on(orderProduct.order.orderNo.eq(order.orderNo))
                .join(product).on(orderProduct.product.productNo.eq(product.productNo))
                .orderBy(order.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = select(order.count()).from(order);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    @Override
    public Page<GetOrderListResponseDto> getOrdersListByUser(Pageable pageable, Long memberNo) {
        JPQLQuery<GetOrderListResponseDto> query = from(order)
                .select(Projections.constructor(
                        GetOrderListResponseDto.class,
                        order.orderNo,
                        order.orderStateCode.codeName,
                        order.createdAt,
                        order.receivedAt,
                        order.invoiceNumber,
                        order.orderPrice
                ))
                .where(order.member.memberNo.eq(memberNo))
                .join(orderProduct).on(orderProduct.order.orderNo.eq(order.orderNo))
                .join(product).on(orderProduct.product.productNo.eq(product.productNo))
                .orderBy(order.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = select(order.count()).from(order).where(order.member.memberNo.eq(memberNo));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }
}
