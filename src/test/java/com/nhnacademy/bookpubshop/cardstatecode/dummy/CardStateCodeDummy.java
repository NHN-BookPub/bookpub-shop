package com.nhnacademy.bookpubshop.cardstatecode.dummy;

import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;

/**
 * 카드결제상태코드 더미 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class CardStateCodeDummy {

    public static CardStateCode dummy(){
        return new CardStateCode(
                0,
                "test_code_name",
                true,
                "test_code_info"

        );
    }


}
