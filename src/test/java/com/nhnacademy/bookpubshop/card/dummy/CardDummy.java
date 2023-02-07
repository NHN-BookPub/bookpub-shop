package com.nhnacademy.bookpubshop.card.dummy;

import com.nhnacademy.bookpubshop.card.entity.Card;

/**
 * 카드 더미 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class CardDummy {

    public static Card dummy() {

        return new Card(
                1L,
                "test_company",
                "test_number",
                1
        );
    }
}
