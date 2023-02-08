package com.nhnacademy.bookpubshop.paymentstatecode.repository.impl;

import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.QPaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 결제상태 레포지토리 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class PaymentStateCodeRepositoryImpl extends QuerydslRepositorySupport
        implements PaymentStateCodeRepositoryCustom {
    public PaymentStateCodeRepositoryImpl() {
        super(PaymentStateCode.class);
    }

    @Override
    public List<GetPaymentStateResponseDto> getAllPaymentState() {
        QPaymentStateCode paymentState = QPaymentStateCode.paymentStateCode;

        return from(paymentState)
                .select(Projections.constructor(
                        GetPaymentStateResponseDto.class,
                        paymentState.codeNo,
                        paymentState.codeName,
                        paymentState.codeUsed,
                        paymentState.codeInfo
                )).fetch();
    }

    @Override
    public Optional<PaymentStateCode> getPaymentStateCode(String state) {
        QPaymentStateCode paymentState = QPaymentStateCode.paymentStateCode;

        return Optional.of(from(paymentState)
                .where(paymentState.codeName.eq(state))
                .select(paymentState).fetchOne());
    }
}
