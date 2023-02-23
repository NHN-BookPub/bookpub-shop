package com.nhnacademy.bookpubshop.order.repository.impl;

import static com.nhnacademy.bookpubshop.state.OrderState.*;
import com.nhnacademy.bookpubshop.card.entity.QCard;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderConfirmResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.repository.OrderRepositoryCustom;
import com.nhnacademy.bookpubshop.orderstatecode.entity.QOrderStateCode;
import com.nhnacademy.bookpubshop.payment.entity.QPayment;
import com.nhnacademy.bookpubshop.pricepolicy.entity.QPricePolicy;
import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
public class OrderRepositoryImpl extends QuerydslRepositorySupport
        implements OrderRepositoryCustom {
    QBookpubOrder order = QBookpubOrder.bookpubOrder;
    QOrderStateCode orderStateCode = QOrderStateCode.orderStateCode;
    QPricePolicy packagingPricePolicy = QPricePolicy.pricePolicy;
    QPricePolicy deliveryPricePolicy = QPricePolicy.pricePolicy;
    QMember member = QMember.member;
    QPayment payment = QPayment.payment;
    QCard card = QCard.card;
    QOrderProduct orderProduct = QOrderProduct.orderProduct;

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
                                member.memberNo,
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
                        .leftJoin(order.member, member)
                        .innerJoin(order.orderStateCode, orderStateCode)
                        .innerJoin(order.deliveryPricePolicy, deliveryPricePolicy)
                        .innerJoin(order.packagingPricePolicy, packagingPricePolicy)
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
    public Page<GetOrderListForAdminResponseDto> getOrderList(Pageable pageable) {

        JPQLQuery<GetOrderListForAdminResponseDto> query = from(order)
                .select(Projections.constructor(GetOrderListForAdminResponseDto.class,
                        order.orderNo.as("orderNo"),
                        member.memberId.as("memberId"),
                        order.createdAt,
                        order.invoiceNumber.as("invoiceNo"),
                        orderStateCode.codeName.as("orderState"),
                        order.orderPrice.as("totalAmount"),
                        order.receivedAt))
                .join(order.orderStateCode, orderStateCode)
                .leftJoin(order.member, member)
                .on(order.member.memberNo.eq(member.memberNo))
                .orderBy(order.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = from(order)
                .select(order.orderNo.count());

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
                .innerJoin(order.member, member)
                .where(order.member.memberNo.eq(memberNo))
                .orderBy(order.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
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

    /**
     * {@inheritDoc}
     */
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
    public Optional<GetOrderConfirmResponseDto> getOrderConfirmInfo(Long orderNo) {
        return Optional.of(from(order)
                .innerJoin(order.orderStateCode, orderStateCode)
                .select(Projections.constructor(GetOrderConfirmResponseDto.class,
                        order.orderName,
                        order.orderBuyer.as("buyerName"),
                        order.orderRecipient.as("recipientName"),
                        order.roadAddress.as("addressBase"),
                        order.addressDetail,
                        order.receivedAt,
                        order.orderRequest,
                        order.orderPrice.as("totalAmount"),
                        order.orderId,
                        orderStateCode.codeName.as("orderState")
                )).where(order.orderNo.eq(orderNo)).fetchOne());
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderProduct> getOrderProductList(Long orderNo) {
        return from(order)
                .innerJoin(orderProduct)
                .on(order.orderNo.eq(orderProduct.order.orderNo))
                .select(orderProduct)
                .where(order.orderNo.eq(orderNo)).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TotalSaleDto> getTotalSale(LocalDateTime start, LocalDateTime end) {

        return from(order)
                .select(Projections.constructor(
                        TotalSaleDto.class,
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CANCEL_PAYMENT.getName()))
                                .then(1)
                                .otherwise((Integer) null)
                                .count()
                                .as("cancelPaymentCnt"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CANCEL_PAYMENT.getName()))
                                .then(order.orderPrice)
                                .otherwise(0L)
                                .sum()
                                .as("cancelPaymentAmount"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CANCEL.getName()))
                                .then(1)
                                .otherwise((Integer) null)
                                .count()
                                .as("cancelOrderCnt"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CONFIRMED.getName()))
                                .then(1)
                                .otherwise((Integer) null)
                                .count()
                                .as("saleCnt"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CONFIRMED.getName()))
                                .then(order.orderPrice)
                                .otherwise(0L)
                                .sum()
                                .as("saleAmount"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName.eq(CONFIRMED.getName()))
                                .then(order.orderPrice)
                                .when(orderStateCode.codeName.eq(CANCEL_PAYMENT.getName()))
                                .then((order.orderPrice.multiply(-1)))
                                .otherwise(0L)
                                .sum()
                                .as("total")
                ))
                .innerJoin(order.orderStateCode, orderStateCode)
                .where(order.createdAt.between(start, end))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TotalSaleYearDto> getTotalSaleMonth(LocalDateTime start, LocalDateTime end) {

        return from(order)
                .select(Projections.constructor(
                        TotalSaleYearDto.class,
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CANCEL_PAYMENT.getName()))
                                .then(1)
                                .otherwise((Integer) null)
                                .count()
                                .as("cancelPaymentCnt"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CANCEL_PAYMENT.getName()))
                                .then(order.orderPrice)
                                .otherwise(0L)
                                .sum()
                                .as("cancelPaymentAmount"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CANCEL.getName()))
                                .then(1)
                                .otherwise((Integer) null)
                                .count()
                                .as("cancelOrderCnt"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CONFIRMED.getName()))
                                .then(1)
                                .otherwise((Integer) null)
                                .count()
                                .as("saleCnt"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName
                                        .eq(CONFIRMED.getName()))
                                .then(order.orderPrice)
                                .otherwise(0L)
                                .sum()
                                .as("saleAmount"),
                        new CaseBuilder()
                                .when(orderStateCode.codeName.eq(CONFIRMED.getName()))
                                .then(order.orderPrice)
                                .when(orderStateCode.codeName.eq(CANCEL_PAYMENT.getName()))
                                .then((order.orderPrice.multiply(-1)))
                                .otherwise(0L)
                                .sum()
                                .as("total"),
                        order.createdAt.month()
                                .as("month")
                ))
                .innerJoin(order.orderStateCode, orderStateCode)
                .groupBy(order.createdAt.month())
                .orderBy(order.createdAt.month().asc())
                .where(order.createdAt.between(start, end))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderCntResponseDto> getOrderTime() {
        return from(order)
                .select(Projections.constructor(
                        OrderCntResponseDto.class,
                        order.createdAt.hour(),
                        order.orderNo.count())
                )
                .from(order)
                .groupBy(order.createdAt.hour())
                .orderBy(order.createdAt.hour().asc())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetOrderListForAdminResponseDto> getOrderListByCodeName(Pageable pageable, String codeName) {
        JPQLQuery<GetOrderListForAdminResponseDto> query = from(order)
                .select(Projections.constructor(GetOrderListForAdminResponseDto.class,
                        order.orderNo.as("orderNo"),
                        member.memberId.as("memberId"),
                        order.createdAt,
                        order.invoiceNumber.as("invoiceNo"),
                        orderStateCode.codeName.as("orderState"),
                        order.orderPrice.as("totalAmount"),
                        order.receivedAt))
                .join(order.orderStateCode, orderStateCode)
                .leftJoin(order.member, member)
                .on(order.member.memberNo.eq(member.memberNo))
                .where(orderStateCode.codeName.eq(codeName))
                .orderBy(order.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = from(order)
                .where(order.orderStateCode.codeName.eq(codeName))
                .innerJoin(orderStateCode)
                .on(orderStateCode.codeNo.eq(order.orderStateCode.codeNo))
                .select(order.orderNo.count());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }
}
