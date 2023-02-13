package com.nhnacademy.bookpubshop.payment.repository.impl;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.response.GetRefundResponseDto;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.entity.QPayment;
import com.nhnacademy.bookpubshop.payment.repository.PaymentRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * 결제 레포지토리 구현.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Repository
public class PaymentRepositoryImpl extends QuerydslRepositorySupport
        implements PaymentRepositoryCustom {
    QPayment payment = QPayment.payment;
    QBookpubOrder order = QBookpubOrder.bookpubOrder;

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetRefundResponseDto> getRefundInfo(
            RefundRequestDto refundRequestDto) {
        return Optional.of(
                from(payment)
                        .select(Projections.constructor(
                                GetRefundResponseDto.class,
                                payment.paymentKey
                        )).where(payment.order.orderNo.eq(refundRequestDto.getOrderNo()))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Payment> getPayment(String paymentKey) {
        return Optional.of(
                from(payment)
                        .select(payment)
                        .where(payment.paymentKey.eq(paymentKey))
                        .fetchOne()
        );
    }

    @Override
    public Optional<BookpubOrder> getOrderByPaymentKey(String paymentKey) {
        return Optional.of(
                from(payment)
                        .innerJoin(payment.order, order)
                        .select(order)
                        .where(payment.paymentKey.eq(paymentKey))
                        .fetchOne()
        );
    }
}
