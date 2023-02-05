package com.nhnacademy.bookpubshop.payment.repository.impl;

import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.repository.PaymentRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 결제 레포지토리 구현.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class PaymentRepositoryImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {
    public PaymentRepositoryImpl() {
        super(Payment.class);
    }
}
