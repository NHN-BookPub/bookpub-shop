package com.nhnacademy.bookpubshop.paymenttypestatecode.repository.impl;

import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.QPaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 결제유형 레포지토리 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class PaymentTypeRepositoryImpl extends QuerydslRepositorySupport
        implements PaymentTypeRepositoryCustom {
    public PaymentTypeRepositoryImpl() {
        super(PaymentTypeStateCode.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPaymentTypeResponseDto> getAllPaymentType() {
        QPaymentTypeStateCode paymentTypeStateCode
                = QPaymentTypeStateCode.paymentTypeStateCode;

        return from(paymentTypeStateCode)
                .select(Projections.constructor(
                        GetPaymentTypeResponseDto.class,
                        paymentTypeStateCode.codeNo,
                        paymentTypeStateCode.codeName,
                        paymentTypeStateCode.codeUsed,
                        paymentTypeStateCode.codeInfo
                )).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PaymentTypeStateCode> getPaymentType(String type) {
        QPaymentTypeStateCode paymentTypeStateCode
                = QPaymentTypeStateCode.paymentTypeStateCode;

        return Optional.of(from(paymentTypeStateCode)
                .select(paymentTypeStateCode).where(paymentTypeStateCode.codeName.eq(type))
                .fetchOne());
    }
}
