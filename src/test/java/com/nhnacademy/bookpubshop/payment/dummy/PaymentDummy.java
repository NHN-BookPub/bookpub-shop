package com.nhnacademy.bookpubshop.payment.dummy;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.time.LocalDateTime;

/**
 * 결제 더미클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class PaymentDummy {

    public static Payment dummy(BookpubOrder order, PaymentStateCode paymentStateCode,
            PaymentTypeStateCode paymentTypeStateCode) {
        return new Payment(
                order,
                paymentStateCode,
                paymentTypeStateCode,
                LocalDateTime.now(),
                "paymentKey",
                "receipt"
        );
    }

}
