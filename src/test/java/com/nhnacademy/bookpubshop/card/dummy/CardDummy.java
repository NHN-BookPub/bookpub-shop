package com.nhnacademy.bookpubshop.card.dummy;

import com.nhnacademy.bookpubshop.card.entity.Card;
import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
import com.nhnacademy.bookpubshop.payment.entity.Payment;

/**
 * 카드 더미 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class CardDummy {

    public static Card dummy(Payment payment, CardStateCode cardStateCode) {

        return new Card(
                payment.getPaymentNo(),
                payment,
                cardStateCode,
                "test_company",
                "test_number",
                true,
                1
        );
    }
}
