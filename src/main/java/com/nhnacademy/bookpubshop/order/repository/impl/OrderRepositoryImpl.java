package com.nhnacademy.bookpubshop.order.repository.impl;

import static com.querydsl.jpa.JPAExpressions.select;

import com.nhnacademy.bookpubshop.card.entity.QCard;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.repository.OrderRepositoryCustom;
import com.nhnacademy.bookpubshop.orderstatecode.entity.QOrderStateCode;
import com.nhnacademy.bookpubshop.payment.entity.QPayment;
import com.nhnacademy.bookpubshop.pricepolicy.entity.QPricePolicy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 주문 레포지토리에서 querydsl 사용을 위한 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Slf4j
public class OrderRepositoryImpl extends QuerydslRepositorySupport
        implements OrderRepositoryCustom {
    QBookpubOrder order = QBookpubOrder.bookpubOrder;
    QOrderStateCode orderStateCode = QOrderStateCode.orderStateCode;
    QPricePolicy packagingPricePolicy = QPricePolicy.pricePolicy;
    QPricePolicy deliveryPricePolicy = QPricePolicy.pricePolicy;
    QMember member = QMember.member;
    QPayment payment = QPayment.payment;
    QCard card = QCard.card;

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
                                packagingPricePolicy.policyFee,
                                deliveryPricePolicy.policyFee,
                                order.orderRequest,
                                order.pointAmount,
                                order.couponDiscount,
                                order.orderPrice,
                                order.orderName,
                                order.orderId
                        ))
                        .innerJoin(order.orderStateCode, orderStateCode)
                        .innerJoin(order.deliveryPricePolicy, packagingPricePolicy)
                        .innerJoin(order.packagingPricePolicy, deliveryPricePolicy)
                        .where(order.orderNo.eq(orderNo))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetOrderDetailResponseDto> getOrderDetailByOrderId(String orderId) {
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
                                packagingPricePolicy.policyFee,
                                deliveryPricePolicy.policyFee,
                                order.orderRequest,
                                order.pointAmount,
                                order.couponDiscount,
                                order.orderPrice,
                                order.orderName,
                                order.orderId
                        ))
                        .innerJoin(order.orderStateCode, orderStateCode)
                        .innerJoin(order.deliveryPricePolicy, packagingPricePolicy)
                        .innerJoin(order.packagingPricePolicy, deliveryPricePolicy)
                        .where(order.orderId.eq(orderId))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetOrderListForAdminResponseDto> getOrdersList(Pageable pageable) {
        JPQLQuery<GetOrderListForAdminResponseDto> query = from(order)
                .select(Projections.constructor(
                        GetOrderListForAdminResponseDto.class,
                        order.orderNo,
                        member.memberId,
                        order.createdAt,
                        order.invoiceNumber,
                        orderStateCode.codeName,
                        order.orderPrice,
                        order.receivedAt
                ))
                .innerJoin(order.member, member)
                .innerJoin(order.orderStateCode, orderStateCode)
                .orderBy(order.orderNo.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = select(order.count()).from(order);


        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetOrderListResponseDto> getOrdersListByUser(Pageable pageable, Long memberNo) {
        JPQLQuery<GetOrderListResponseDto> query = from(order)
                .select(Projections.constructor(
                        GetOrderListResponseDto.class,
                        order.orderNo,
                        orderStateCode.codeName,
                        order.createdAt,
                        order.receivedAt,
                        order.invoiceNumber,
                        order.orderPrice
                ))
                .innerJoin(order.orderStateCode, orderStateCode)
                .where(order.member.memberNo.eq(memberNo))
                .orderBy(order.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count =
                select(order.orderNo.count())
                        .from(order)
                        .innerJoin(order.member, member)
                        .where(member.memberNo.eq(memberNo));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetOrderVerifyResponseDto> verifyPayment(String orderId) {
        return Optional.of(from(order)
                .select(Projections.constructor(GetOrderVerifyResponseDto.class,
                        order.orderPrice))
                .where(order.orderId.eq(orderId))
                .fetchOne()
        );
    }

    @Override
    public Optional<BookpubOrder> getOrderByOrderKey(String orderId) {
        return Optional.of(from(order)
                .select(order)
                .where(order.orderId.eq(orderId)).fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetOrderAndPaymentResponseDto> getOrderAndPayment(String orderId) {
        return Optional.of(
                from(order)
                        .innerJoin(payment)
                        .on(order.orderNo.eq(payment.order.orderNo))
                        .leftJoin(card)
                        .on(payment.paymentNo.eq(card.paymentNo))
                        .select(Projections.constructor(
                                GetOrderAndPaymentResponseDto.class,
                                order.orderName,
                                order.roadAddress
                                        .concat(" ")
                                        .concat(order.addressDetail)
                                        .as("address"),
                                order.orderRecipient,
                                order.receivedAt,
                                order.orderPrice,
                                order.pointSave,
                                card.cardCompany,
                                payment.receipt))
                        .where(order.orderId.eq(orderId)).fetchOne());
    }
}
