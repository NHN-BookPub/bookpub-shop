package com.nhnacademy.bookpubshop.paymentstatecode.dummy;

import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;

/**
 * 결제상태코드 더미클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class PaymentStateCodeDummy {
    public static PaymentStateCode dummy() {

        return new PaymentStateCode(
                null,
                "test_codeName",
                true,
                "test_codeInfo"
        );
    }
}
